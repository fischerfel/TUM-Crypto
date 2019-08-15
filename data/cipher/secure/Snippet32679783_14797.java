public void decrypt(String filename) throws Exception {
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream("filename"));
    Base64InputStream base64Stream = new Base64InputStream(bis, 0);
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    CipherInputStream cis = new CipherInputStream(base64Stream, c);
    byte[] plainBuf = new byte[2048];
    int nRead;
    while ((nRead = cis.read(plainBuf)) > 0) {
        // send plainBuf[0] through plainBuf[nRead-1] to the video codec
    }
    cis.close();
}
