public static String encode(String str) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.reset();
    return (new BASE64Encoder()).encode(md.digest(str.getBytes("UTF-8")));
}
