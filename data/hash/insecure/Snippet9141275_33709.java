public static String getMD5ChecksumByFlash(String filename) throws Exception {
    InputStream fis = new FileInputStream(filename);
    byte[] buffer = new byte[100];
    MessageDigest complete = MessageDigest.getInstance("MD5");

    int passes = fis.available() / 100;
    int currentOffset = 0;
    int readBytes = 0;

    for (int i = 0; i < 100; i++) {
        readBytes += fis.read(buffer, i, 1);
        // TODO: Check for I/O errors
        fis.skip(passes - 1);
    }
    fis.close();

    complete.update(buffer, 0, readBytes);

    byte[] b = complete.digest();
    String result = "";
    for (int i = 0; i < b.length; i++) {
        result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
}
