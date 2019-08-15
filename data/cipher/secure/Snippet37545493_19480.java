public class DataDecryptorNew {
private static final int PUBLIC_KEY_SIZE = 294;
private static final int EID_SIZE = 32;
private static final int SECRET_KEY_SIZE = 256;
private static final String TRANSFORMATION2 = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
private static final String TRANSFORMATION3 = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
private static final String SECURITY_PROVIDER = "BC";
private static SunPKCS11 providerPKCS11;
private static String provider;
private static final String DIGEST_ALGORITHM = "SHA-256";
private static final String MASKING_FUNCTION = "MGF1";
private static final int VECTOR_SIZE = 16;
private static final int HMAC_SIZE = 32;
private static final int BLOCK_SIZE = 128;
private static final byte[] HEADER_DATA = "VERSION_1.0".getBytes();
private static final String SIGNATURE_TAG = "Signature";
private static final String MEC_TYPE = "DOM";

public static final String DLL = "C:\\pkcs11\\cknfast.dll";
public static String alias = "";
public static int keyLength = 2048;

private static final String password = "";
public static final String storeType = "PKCS11-nCipher";

private PrivateKey privateKey;
private PublicKey publicKey;
private KeyStore.PrivateKeyEntry keyEntry;
private KeyStore keyEntry1;

static {
    Security.addProvider(new BouncyCastleProvider());
}
public byte[] decrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
        NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IOException, CertificateException, Exception {
    if (data == null || data.length == 0) {
        throw new Exception("byte array data can not be null or blank array.");
    }
    PrivateKey key = getPrivateKey();
    ByteArraySpliter arrSpliter = new ByteArraySpliter(data);
    byte[] secretKey = decryptSecretKeyData(arrSpliter.getEncryptedSecretKey(), arrSpliter.getIv(), key);
    byte[] plainData = decryptData(arrSpliter.getEncryptedData(), arrSpliter.getIv(), secretKey);
    boolean result = validateHash(plainData);
    if (!result) {
        throw new Exception("Integrity Validation Failed : "
                + "The original data at client side and the decrypted data at server side is not identical");
    }
    return trimHMAC(plainData);
}
private KeyStore.PrivateKeyEntry getKeyFromFile(String keyStoreFile, char[] keyStorePassword) {

    try {
        // Load the KeyStore and get the signing key and certificate.
        KeyStore ks = KeyStore.getInstance("PKCS12");
        FileInputStream keyFileStream = new FileInputStream(keyStoreFile);
        ks.load(keyFileStream, keyStorePassword);
        String alias = ks.aliases().nextElement();

        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(keyStorePassword));

        if (entry == null) {
            throw new Exception("Key not found for the given alias.");
        }

        keyFileStream.close();

        return entry;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


private static PrivateKey getPrivateKey() throws Exception {
    String config = "name=nCipher\n"
            + "library=" + DLL + "\n"
            + "slotListIndex = 0 ";

    ByteArrayInputStream bais = new ByteArrayInputStream(config.getBytes());
    Provider p = new SunPKCS11(bais);

    Security.addProvider(p);
    KeyStore ks = KeyStore.getInstance("PKCS11", p);
    ks.load(null, "".toCharArray());
    System.out.println("Keystore size : " + ks.size());

    String alias = "ncipher-cert/cn=(n)code solutions ca 2014,2.5.4.51=#13133330312c20474e464320496e666f746f776572,street=bodakdev\\, s g road\\, ahmedabad,st=gujarat,2.5.4.17=#1306333830303534,ou=certifying authority,o=gujarat narmada valley fertilizers and chemicals limited,c=in/1396768448";

    KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
    KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, protParam);
    // get my private key
    PrivateKey key = pkEntry.getPrivateKey();
    if (key instanceof PrivateKey) {
        // Get certificate of public key
        Certificate cert = ks.getCertificate(alias);
        System.out.println(">>>>>>>>>" + ((X509Certificate) cert).getSerialNumber().toString(16));

        // Get public key
        PublicKey publicKey = cert.getPublicKey();

        //Get Private Key
        Key privatekey = (PrivateKey) key;
        System.out.println("privatekey=" + privatekey);
    }

    return (PrivateKey) key;
}

private byte[] decryptSecretKeyData(byte[] encryptedSecretKey, byte[] iv) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
        NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IOException, CertificateException, Exception {
    try {

        PrivateKey key = (PrivateKey) getPrivateKey();
        System.out.println("Private Key:" + getPrivateKey().getFormat());
        Cipher decCipher = Cipher.getInstance("RSA/ECB/NoPadding");
        decCipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decipheredText = null;
        decipheredText = decCipher.doFinal(encryptedSecretKey);
        System.out.println("OAEP padded plain text: " + Arrays.toString(decipheredText));
        if (decipheredText.length < keyLength / 8) {
            byte[] tmp = new byte[(keyLength / 8) - 42];
            System.arraycopy(decipheredText, 0, tmp, tmp.length - decipheredText.length, decipheredText.length);
            System.out.println("Zero padding to " + (keyLength / 8));
            decipheredText = tmp;
        }
        PSource pSrc = (new PSource.PSpecified(new byte[256]));
        OAEPParameterSpec paramSpec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, pSrc);
        RSAPadding padding = RSAPadding.getInstance(RSAPadding.PAD_OAEP_MGF1, keyLength / 8, new SecureRandom(), paramSpec);
        System.out.println("PaddedPlainText length: " + decipheredText.length); //256
        byte[] plainText2 = padding.unpad(decipheredText, 0, decipheredText.length);
        System.out.println("Unpadded plain text: " + DatatypeConverter.printHexBinary(plainText2));
        System.out.println("Decrypted Value:" + new String(plainText2));
        return plainText2;
    } catch (GeneralSecurityException e) {
        e.printStackTrace();
        throw new Exception("Failed to decrypt AES secret key using RSA.", e);
    }
}


private static class ByteArraySpliter {

    private final byte[] headerVersion;
    private final byte[] iv;
    private final byte[] encryptedSecretKey;
    private final byte[] encryptedData;
    private final byte[] publicKeyData;

    public ByteArraySpliter(byte[] data) throws Exception {
        int offset = 0;
        headerVersion = new byte[HEADER_DATA.length];
        copyByteArray(data, 0, headerVersion.length, headerVersion);
        offset = offset + HEADER_DATA.length;
        publicKeyData = new byte[PUBLIC_KEY_SIZE];
        copyByteArray(data, offset, publicKeyData.length, publicKeyData);
        offset = offset + PUBLIC_KEY_SIZE;
        iv = new byte[EID_SIZE];
        copyByteArray(data, offset, iv.length, iv);
        offset = offset + EID_SIZE;
        encryptedSecretKey = new byte[SECRET_KEY_SIZE];
        copyByteArray(data, offset, encryptedSecretKey.length, encryptedSecretKey);
        offset = offset + SECRET_KEY_SIZE;
        encryptedData = new byte[data.length - offset];
        copyByteArray(data, offset, encryptedData.length, encryptedData);
    }

    public byte[] getIv() {
        return iv;
    }

    public byte[] getEncryptedSecretKey() {
        return encryptedSecretKey;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    private void copyByteArray(byte[] src, int offset, int length, byte[] dest) throws Exception {
        try {
            System.arraycopy(src, offset, dest, 0, length);
        } catch (Exception e) {

            throw new Exception("Decryption failed, Corrupted packet ", e);
        }
    }
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
    return new byte[][]{l, r};
}

public byte[] generateHash(byte[] message) throws Exception {
    byte[] hash = null;
    try {
        MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM, SECURITY_PROVIDER);
        digest.reset();
        hash = digest.digest(message);
    } catch (GeneralSecurityException e) {
        throw new Exception("SHA-256 Hashing algorithm not available");
    }
    return hash;
}


public static void main(String[] args) throws Exception {
    String var = "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";
    CustomBase64 base64 = new CustomBase64();
    DataDecryptorNew decryptor = new DataDecryptorNew();
    decryptor.decrypt(Base64.decode(var.getBytes()));
}
