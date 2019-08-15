private String encrypt(String str) throws Exception {
    ClassPathResource classPathResource = new ClassPathResource("testcert1.crt");
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    X509Certificate certificate = (X509Certificate)certificateFactory.generateCertificate(classPathResource.getInputStream());
    PublicKey pk = certificate.getPublicKey();
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, pk);
    return Base64.encodeBase64String(cipher.doFinal(str.getBytes()));
}
