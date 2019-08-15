    public static void encryptFile(String path, byte[] key) throws Exception {

    Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec k = new SecretKeySpec(key, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    RandomAccessFile raf = new RandomAccessFile(path, "rw");

    byte[] buf = new byte[128];
    int bytesRead = 0;
    int totalBytes = 0;
    byte[] output;
    while ((bytesRead = raf.read(buf)) >= 0) {
        output = c.update(buf, 0, bytesRead);
        raf.seek(totalBytes);
        raf.write(output);
        totalBytes += output.length;
        raf.seek(totalBytes+16);
    }
    output = c.doFinal();
    raf.seek(totalBytes);
    raf.write(output);
    raf.getFD().sync();
    raf.close();

}
