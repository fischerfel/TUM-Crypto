public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException { 
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.update(text.getBytes("iso-8859-1"));
    byte[] hash = md.digest();

    Formatter formatter = new Formatter();
    for (byte b : hash)
        formatter.format("%02x", b);
    return formatter.toString();
}
