private static final Logger LOG = Logger.getLogger(com.form.modelcontroller.DataDecryptorHSM.class);
private static final String TRANSFORMATION = "RSA/ECB/NOPADDING";

private static final String DIGEST_ALGORITHM = "SHA-256";

/* change PKCS12 to PKCS11 */
private static final String KEY_STORE_TYPE_DONGLE = "PKCS11";

private static PrivateKey privateKey;
private static PublicKey publicKeyFile;
private static Provider provider;

private static final BigInteger EXPONENT = new BigInteger("1", 16);

/********* FOR SIGN *******************/
private static KeyStore.PrivateKeyEntry keyEntry;
private static final String MEC_TYPE = "DOM";
private static final String WHOLE_DOC_URI = "";

public DataDecryptorHSM(String keyStoreFile, char[] keyStorePassword) {

    LOG.info("***** *************** Inside Constructor Total Provider " + Security.getProviders().length + " *****");
    try {
        provider = new sun.security.pkcs11.SunPKCS11(keyStoreFile);
        Security.addProvider(provider);
        privateKey = getPrivateKeyFromDongle(keyStorePassword);

        if (privateKey == null) {
            LOG.info("Key could not be read for digital signature. Please check value of signature alias and signature password, and restart the Auth Client");
            /*
             * throw new RuntimeException(
             * "Key could not be read for digital signature. Please check value of signature " +
             * "alias and signature password, and restart the Auth Client");
             */
        }

    } catch (Exception e) {
        LOG.info("********* INSIDE CATCH" + e.toString() + "*********");
    }
}

public byte[] decrypt(byte[] data) throws Exception {
    if (data == null || data.length == 0)
        throw new Exception("byte array data can not be null or blank array.");

    // LOG.info("***************************Going for Splitter****************************************");
    ByteArraySpliter arrSpliter = new ByteArraySpliter(data);
    byte[] secretKey = decryptSecretKeyData(arrSpliter.getEncryptedSecretKey(), arrSpliter.getIvPadding(),privateKey);

    LOG.info("*******************Going for Plain Data Decryption****************************");
    byte[] plainData = decryptData(arrSpliter.getEncryptedData(), arrSpliter.getIvPadding(), secretKey);

    // boolean result = validateHash(plainData);
    /* for temprary */
    // if (!result)
    // throw new Exception("Integrity Validation Failed : "
    // + "The original data at client side and the decrypted data at server side is not identical");

    LOG.info("*****************Going for trimHMAC(plainData)****************************");
    return trimHMAC(plainData);
}

private byte[] decryptSecretKeyData(byte[] encryptedSecretKey, byte[] iv, PrivateKey privateKey) throws Exception {
    try {

        LOG.info("**************Inside decryptSecretKeyData***********************");

        **Cipher rsaCipher = Cipher.getInstance(TRANSFORMATION, provider);**
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey); // decrypting the session key with rsa no padding.

        /* The reason is RSA OAEP SHA256 is not supported in HSM and Java 7 */
        LOG.info("******************rsaCipher.doFinal(encryptedSecretKey)*****************************");
        byte[] decKey = rsaCipher.doFinal(encryptedSecretKey);

        // deckey is the decrypted aes key.. without padding... so.. lets see this value in debug
         System.out.print("  publickeyFile from dongle :" + publicKeyFile);
         System.out.print("  decKey :" + new String(decKey));

        // Applying the OAEP padding to get the actual session key.
        LOG.info("************new OAEPEncoding(new RSAEngine(), new SHA256Digest(), iv)*************");
        OAEPEncoding encode = new OAEPEncoding(new RSAEngine(), new SHA256Digest(), iv);

        LOG.info("******************RSAPublicKey rsaPublickey = (*****************************");
        java.security.interfaces.RSAPublicKey rsaPublickey = (java.security.interfaces.RSAPublicKey) publicKeyFile;
        RSAKeyParameters keyParams = new RSAKeyParameters(false, rsaPublickey.getModulus(), EXPONENT);
        encode.init(false, keyParams);

        LOG.info("******************encode.processBlock(decKey, 0, decKey.length);************************");
        byte decryptedSecKey[] = encode.processBlock(decKey, 0, decKey.length);

        // LOG.info("***************************return b;****************************************");
        return decryptedSecKey;
    } catch (InvalidCipherTextException e) {
        LOG.info("*******************Failed to decrypt AES secret key using RSA :**********************");
        e.printStackTrace();
        throw new Exception("Failed to decrypt AES secret key using RSA :" + e.toString());
    }
}

public String byteArrayToHexString(byte[] bytes) {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
        result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    // System.out.println(" byteArrayToHexString now...");
    // System.out.println(" memory now : "+ Runtime.getRuntime().freeMemory());
    bytes = null;
    System.gc();
    return result.toString();
}

private byte[] decryptData(byte[] encryptedData, byte[] eid, byte[] secretKey) throws Exception {
    try {
        byte[][] iv = split(eid, VECTOR_SIZE);

        CFBBlockCipher cfbBlock = new CFBBlockCipher(new AESEngine(), BLOCK_SIZE);
        BufferedBlockCipher cipher = new BufferedBlockCipher(cfbBlock);
        KeyParameter key = new KeyParameter(secretKey);

        cipher.init(false, new ParametersWithIV(key, iv[0]));

        int outputSize = cipher.getOutputSize(encryptedData.length);

        byte[] result = new byte[outputSize];
        int processLen = cipher.processBytes(encryptedData, 0, encryptedData.length, result, 0);
        cipher.doFinal(result, processLen);
        return result;
    } catch (InvalidCipherTextException txtExp) {
        throw new Exception("Decrypting data using AES failed", txtExp);
    }
}

private byte[] trimHMAC(byte[] decryptedText) {
    byte[] actualText;
    if (decryptedText == null || decryptedText.length <= HMAC_SIZE) {
        actualText = new byte[0];
    } else {
        actualText = new byte[decryptedText.length - HMAC_SIZE];
        System.arraycopy(decryptedText, HMAC_SIZE, actualText, 0, actualText.length);
    }
    return actualText;
}


private byte[][] split(byte[] src, int n) {
    byte[] l, r;
    if (src == null || src.length <= n) {
        l = src;
        r = new byte[0];
    } else {
        l = new byte[n];
        r = new byte[src.length - n];
        System.arraycopy(src, 0, l, 0, n);
        System.arraycopy(src, n, r, 0, r.length);
    }
    return new byte[][] { l, r };
}

public byte[] generateHash(byte[] message) throws Exception {
    byte[] hash = null;
    try {
        /* Registering the Bouncy Castle as the RSA provider.*/
        // MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM, SECURITY_PROVIDER);
        MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM, provider);
        digest.reset();
        hash = digest.digest(message);
    } catch (GeneralSecurityException e) {
        throw new Exception("SHA-256 Hashing algorithm not available");
    }
    return hash;
}

/* this is where i am getting private key */
private static PrivateKey getPrivateKeyFromDongle(char[] keyStorePassword) {
    LOG.info("***********Inside of getPrivateKeyFromDongle()***********");

    KeyStore ks;
    try {
        ks = KeyStore.getInstance(KEY_STORE_TYPE_DONGLE);
        ks.load(null, keyStorePassword);

        // ByteArrayInputStream is1 = new ByteArrayInputStream(("slot:1").getBytes());
        // ks = KeyStore.getInstance("Luna");

        // LunaProvider
        // ks.load(is1, keyStorePassword);

        Enumeration<String> alias = ks.aliases();
        String signAlias = "";

        while (alias.hasMoreElements()) {
            String aliasName = alias.nextElement();

            X509Certificate cert = (X509Certificate) ks.getCertificate(aliasName);
            boolean[] keyUsage = cert.getKeyUsage();

            // for (int i = 0; i < keyUsage.length; i++) {
            // if ((i == 0 || i == 1) && keyUsage[i] == true) {
            // signAlias = aliasName;
            // LOG.info("First  Inside Loop--> " + signAlias);
            // break;
            // }
            // }
            boolean isbreak = false;
            for (int i = 0; i < keyUsage.length; i++) {
                if (keyUsage[i]) {

                    /* */
                    isbreak = true;
                    // LOG.info("aliasName --> " + aliasName);
                    publicKeyFile = cert.getPublicKey();

                    // System.out.println("Public Key-->" + publicKeyFile.toString());
                    // String publicExponent = ((RSAPublicKey) publicKeyFile).getPublicExponent().toString(16);
                    // String publicModulus = ((RSAPublicKey) publicKeyFile).getModulus().toString(16);
                    // System.out.println("  publicExponent : " + publicExponent);
                    // System.out.println("  publicModulus : " + publicModulus);
                    signAlias = aliasName;
                    // LOG.info("Second  Inside Loop--> " + signAlias);
                    break;
                }
            }
            if (isbreak)
                break;
        }

        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keyStorePassword);

        LOG.info("******** Initializing key Entry ********");
        keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(signAlias, protParam);

        return keyEntry.getPrivateKey();

    } catch (KeyStoreException e) {
         e.printStackTrace();
        LOG.error(StackTraceUtil.getStackTrace(e));
    } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
        LOG.error(StackTraceUtil.getStackTrace(e));
    } catch (UnrecoverableEntryException e) {
         e.printStackTrace();
        LOG.error(StackTraceUtil.getStackTrace(e));
    } catch (CertificateException e) {
         e.printStackTrace();
        LOG.error(StackTraceUtil.getStackTrace(e));
    } catch (IOException e) {
         e.printStackTrace();
        LOG.error(StackTraceUtil.getStackTrace(e));
    }
    return null;
}

/*************************** For Sign Request ***************************/

/**
 * Method to digitally sign an XML document.
 * 
 * @param xmlDocument
 *            - Input XML Document.
 * @return Signed XML document
 */
public String signXML(String xmlDocument, boolean includeKeyInfo) {

    LOG.info("***********Inside of Sign of XML signXML()***********");

    StringWriter stringWriter = null;
    // if (this.provider == null) {
    // this.provider = new BouncyCastleProvider();
    // // Security.addProvider(this.provider);
    // }

    try {
        // Parse the input XML
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document inputDocument = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xmlDocument)));

        /* Sign the input XML's DOM document*/
        Document signedDocument = sign(inputDocument, includeKeyInfo);

        /* Convert the signedDocument to XML String*/
        stringWriter = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(signedDocument), new StreamResult(stringWriter));

    } catch (Exception e) {
        LOG.error(StackTraceUtil.getStackTrace(e));
         e.printStackTrace();
        // throw new RuntimeException("Error while digitally signing the XML document", e);
    }
    return stringWriter.getBuffer().toString();
}

private Document sign(Document xmlDoc, boolean includeKeyInfo) throws Exception {
    LOG.info("***********Inside of Sign()***********");
    if (System.getenv("SKIP_DIGITAL_SIGNATURE") != null) {
        return xmlDoc;
    }

    /* Creating the XMLSignature factory.*/
    XMLSignatureFactory fac = XMLSignatureFactory.getInstance(MEC_TYPE);

    /* Creating the reference object, reading the whole document for signing.*/
    Reference ref = fac.newReference(WHOLE_DOC_URI, fac.newDigestMethod(DigestMethod.SHA1, null),
            Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null,
            null);

    /* Create the SignedInfo.*/
    SignedInfo sInfo = fac.newSignedInfo(
            fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
            fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

    if (keyEntry == null) {
        throw new RuntimeException(
                "Key could not be read for digital signature. Please check value of signature alias and signature password, and restart the Auth Client");
    }

    LOG.info("***********Inside of Sign()--X509Certificate x509Cert***********");
    X509Certificate x509Cert = (X509Certificate) keyEntry.getCertificate();

    KeyInfo kInfo = getKeyInfo(x509Cert, fac);
    DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), xmlDoc.getDocumentElement());
    XMLSignature signature = fac.newXMLSignature(sInfo, includeKeyInfo ? kInfo : null);
    signature.sign(dsc);

    Node node = dsc.getParent();
    return node.getOwnerDocument();

}

@SuppressWarnings("unchecked")
private KeyInfo getKeyInfo(X509Certificate cert, XMLSignatureFactory fac) {
    LOG.info("***********Inside  KeyInfo getKeyInfo(X509Certificate cert)***********");

    /* Create the KeyInfo containing the X509Data.*/
    KeyInfoFactory kif = fac.getKeyInfoFactory();
    List x509Content = new ArrayList();
    x509Content.add(cert.getSubjectX500Principal().getName());
    x509Content.add(cert);
    X509Data xd = kif.newX509Data(x509Content);
    return kif.newKeyInfo(Collections.singletonList(xd));
}

public static void listProviders() {
    Provider[] providers = Security.getProviders();
    System.out.println("Provider list");
    for (int i = 0; i < providers.length; i++) {
        System.out.println((i + 1) + ":" + providers[i].toString());
    }
    System.out.println();
}
