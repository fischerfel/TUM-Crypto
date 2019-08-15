public void encryptFile() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, CertificateException, KeyStoreException, IOException {

    FileInputStream fis = new FileInputStream("c:\\sample.pdf");
    FileOutputStream fos = new FileOutputStream("c:\\sample_encrypted");

    Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    c.init(Cipher.ENCRYPT_MODE, getSapPublicCertificate().getPublicKey());
    CipherOutputStream cos = new CipherOutputStream(fos, c);

    byte[] buf = new byte[2048];
    int read;
    while ((read = fis.read(buf)) != -1) {
        cos.write(buf, 0, read);
    }

    fis.close();
    cos.flush();
    cos.close();
}
