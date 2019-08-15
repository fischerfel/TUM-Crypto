public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md;
    md = MessageDigest.getInstance("SHA-1");
    byte[] sha1hash = new byte[40];
    md.update(text.getBytes("UTF-8"), 0, text.length());
    sha1hash = md.digest();
    //String converted = convertToHex(sha1hash);
    String converted = getHexString(sha1hash);
    return converted;
}
