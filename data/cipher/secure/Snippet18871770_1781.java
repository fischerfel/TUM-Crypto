private static byte[] doThis(String message) {
    byte[] messageCrypte = null;
       try {
        // Certificate Input Stream
        // LA SSL Certificate to be passed.
        InputStream inStream = new FileInputStream(certificate);

        // X509Certificate created
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
        inStream.close();

        // Getting Public key using Certficate
        PublicKey rsaPublicKey = (PublicKey) cert.getPublicKey();

        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "SunJCE");
        encryptCipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);

        byte[] messageACrypter = message.getBytes();
        // Encrypted String
        messageCrypte = encryptCipher.doFinal(messageACrypter);
       } catch (Exception e) {
        // TODO: Exception Handling
        e.printStackTrace();
       }
    return messageCrypte;
}
