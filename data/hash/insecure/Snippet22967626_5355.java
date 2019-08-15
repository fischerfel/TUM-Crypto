public static String toSHA1(String toEncrypt) {
    return toSHA1(toEncrypt, "UTF-8");
}

public static String toSHA1(String toEncrypt, String encoding) {
    String salt = "fE4wd#u*d9b9kdKszgè02ep5à4qZa!éi6";
    String res = null;
    toEncrypt = toEncrypt + salt;
    try {
        byte[] dataBytes = toEncrypt.getBytes(encoding);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        res = Base64.encodeBytes(md.digest(dataBytes));
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return res;
}
