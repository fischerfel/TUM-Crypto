public static byte[] sha2(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    final MessageDigest mesd = MessageDigest.getInstance("SHA-256");
    mesd.update(text.getBytes("iso-8859-1"), 0, text.length());
    return mesd.digest();
}
