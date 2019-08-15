public static void main(String[] args) {

    Path path = Paths.get(PATH_DOC_NF1);
    try {
        byte[] data = Files.readAllBytes(path);
        Security.addProvider(new IAIK()); 

        KeyStore ks = KeyStore.getInstance("IAIKKeyStore", "IAIK");
        InputStream is = new FileInputStream(PATH_KEYSTORE_TELEMACO);
        X509Certificate certX509;
        PrivateKey pk;

        String pathout=PATH_DOC_NF1+".p7m";

        File file = new File(pathout);

        if (file.exists()){
            System.out.println("Il file " + pathout +" esiste");
        }else if (file.createNewFile()){
            System.out.println("Il file " + pathout +" E' stato creato");
        }else{
            System.out.println("Il file " + pathout +" non puo' essere creato");
        }
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        if (ks != null){
            ks.load(is, KEY_PASSWORD_TELEMACO);
            Enumeration<String> aliases = ks.aliases();
            for (; aliases.hasMoreElements(); ) {
                String alias = aliases.nextElement();
                if(ks.isKeyEntry(alias)){
                    pk = (PrivateKey) ks.getKey(alias,KEY_PASSWORD_TELEMACO);
                    Certificate[] chain = ks.getCertificateChain(alias);
                    certX509 = (X509Certificate) chain[0];


                    MessageDigest digestEngine = MessageDigest.getInstance("SHA");

                    ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();

                    byte[] contentHash = digestEngine.digest(data);

                    contentBuffer.close();

                    SignedData signedData = new SignedData(contentBuffer.toByteArray(), SignedData.IMPLICIT);

                    signedData.setCertificates(new X509Certificate[] { certX509 });

                    SignerInfo signerInfo = new SignerInfo(new IssuerAndSerialNumber(certX509), AlgorithmID.sha, null);

                    Attribute[] authenticatedAttributes = {
                            new Attribute(ObjectID.contentType,new ASN1Object[] {ObjectID.pkcs7_data}),
                            new Attribute(ObjectID.signingTime,new ASN1Object[] {new ChoiceOfTime().toASN1Object()}),
                            new Attribute(ObjectID.messageDigest,new ASN1Object[] {new OCTET_STRING(contentHash)})
                    };

                    signerInfo.setAuthenticatedAttributes(authenticatedAttributes);

                    byte[] toBeSigned = DerCoder.encode(ASN.createSetOf(authenticatedAttributes, true));

                    MessageDigest md = MessageDigest.getInstance("SHA");

                    byte[] hashToBeSigned = md.digest(toBeSigned);

                    DigestInfo digestInfoEngine = new DigestInfo(AlgorithmID.sha1WithRSAEncryption, hashToBeSigned);
                    byte[] toBeEncrypted = digestInfoEngine.toByteArray();


                    Signature sign = Signature.getInstance("RSA");
                    sign.initSign(pk);

                    sign.update(toBeEncrypted);
                    byte[] signatureValue = sign.sign();

                    signerInfo.setEncryptedDigest(signatureValue);

                    signedData.addSignerInfo(signerInfo);

                    OutputStream signatureOutput = new FileOutputStream(pathout);

                    ContentInfoStream cis = new ContentInfoStream(signedData);
                    BufferedOutputStream bos = new BufferedOutputStream(signatureOutput);

                    Base64OutputStream b64Out = new Base64OutputStream(bos);

                    cis.writeTo(b64Out);
                    b64Out.close();
                    in.close();

                    /****VERIFY_SIGN******/

                    InputStream encodedStream;

                    encodedStream = new FileInputStream(pathout);
                    ASN1InputStream asn1 = new ASN1InputStream(encodedStream);
                    ContentInfoStream cis2 = new ContentInfoStream(asn1);

                    SignedDataStream signedData2;

                    if (cis.getContentType().equals(ObjectID.cms_signedData)){

                        signedData2 = (SignedDataStream)cis2.getContent();  
                        InputStream contentStream = signedData2.getInputStream();

                        byte[] read = new byte[10240];

                        int byteRead;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        while((byteRead = contentStream.read(read)) > 0){
                            baos.write(read,0,byteRead);
                        }

                        SignerInfo[] signerInfos = signedData2.getSignerInfos();
                        System.out.println(pathout+"\n\n"+signerInfos[0].toString());

                        for (int i=0; i < signerInfos.length; i++){
                            try
                            {
                                X509Certificate signerCertificate = signedData.verify(i);
                            }
                            catch (SignatureException ex)
                            {
                                try {
                                    System.out.println("Signature ERROR from signer with certificate: "+signedData.getCertificate(signerInfos[i].getIssuerAndSerialNumber()));

                                } catch (PKCSException e) {
                                    e.printStackTrace();
                                }
                                ex.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        System.out.println("errore, ci aspettiamo un SignedData");
                    }
                }
            }
        }


    } catch (IOException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
        e.printStackTrace();
    } catch (CodingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (SignatureException e) {
        e.printStackTrace();
    } catch (PKCSException e) {
        e.printStackTrace();
    }

}
