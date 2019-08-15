public static String encyptPassword (String in) throws UnsupportedEncodingException, NoSuchAlgorithmException{
    byte[] bytes=in.getBytes("UTF-8");
    MessageDigest md=MessageDigest.getInstance(MGF1ParameterSpec.SHA1.getDigestAlgorithm());
    md.update(bytes);
    byte[] digest=md.digest();
    return toHex(digest);
}
