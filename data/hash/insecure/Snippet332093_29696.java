public static String getMD5(String filename)
        throws NoSuchAlgorithmException, IOException {
    MessageDigest messageDigest = 
        java.security.MessageDigest.getInstance("MD5");

    InputStream in = new FileInputStream(filename);

    byte [] buffer = new byte[8192];
    int len = in.read(buffer, 0, buffer.length);

    while (len > 0) {
        messageDigest.update(buffer, 0, len);
        len = in.read(buffer, 0, buffer.length);
    }
    in.close();

    return new BigInteger(1, messageDigest.digest()).toString(16);
}
