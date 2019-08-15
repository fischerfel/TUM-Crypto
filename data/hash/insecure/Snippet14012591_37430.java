public static String calculateStringHash(String text, String encoding) 
        throws NoSuchAlgorithmException, UnsupportedEncodingException{
    MessageDigest md = MessageDigest.getInstance("MD5");
    return getHex(md.digest(text.getBytes(encoding)));
}
