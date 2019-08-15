    public static String SHA256 (String text) throws NoSuchAlgorithmException,    UnsupportedEncodingException
    {
textByte = text.getBytes("UTF-8");  
MessageDigest md = MessageDigest.getInstance("SHA-256");
textByte = md.digest(textByte);
return Base64.encodeToString(textByte,Base64.NO_CLOSE);
}
