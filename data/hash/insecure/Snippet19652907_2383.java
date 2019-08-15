public static String getSHA1FromFileContent(String filename)
        throws NoSuchAlgorithmException, IOException {

    final MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

    InputStream is = new BufferedInputStream(new FileInputStream(filename));
    final byte[] buffer = new byte[1024];

    for (int read = 0; (read = is.read(buffer)) != -1;) {
        messageDigest.update(buffer, 0, read);
    }

    is.close();

    // Convert the byte to hex format
    Formatter formatter = new Formatter();

    for (final byte b : messageDigest.digest()) {
        formatter.format("%02x", b);
    }

    String res = formatter.toString();

    formatter.close();

    return res;
}
