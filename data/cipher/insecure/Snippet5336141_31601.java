    public static void encryptFile(String path, byte[] key) throws Exception {

    Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec k = new SecretKeySpec(key, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    RandomAccessFile raf = new RandomAccessFile(path, "rw");

    byte[] buf = new byte[256];
    byte[] output;
    int bytesRead = 0;
    int totalBytes = 0;
    while ((bytesRead = raf.read(buf)) >= 0) {
        int len = buf.length;
        if (bytesRead < len) {
            byte[] out2 = c.doFinal(buf, 0 , bytesRead);
            raf.seek(totalBytes);
            raf.write(out2);
        } else {
        output = c.update(buf, 0, bytesRead);
        raf.seek(totalBytes);
        raf.write(output);
        }
        totalBytes += bytesRead;
        }
    raf.getFD().sync();
    raf.close();
    }
