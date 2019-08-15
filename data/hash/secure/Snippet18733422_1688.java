public static String SHA(String text)
        throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md;
    md = MessageDigest.getInstance("SHA-256");
    byte[] md5 = new byte[64];
    md.update(text.getBytes("iso-8859-1"), 0, text.length());
    md5 = md.digest();
    return convertedToHex(md5);
}
