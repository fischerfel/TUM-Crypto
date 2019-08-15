public static String encryptPassword(String password) {
    String hash = null;
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes("UTF-8"));
        byte[] raw = md.digest();
        hash = (new BASE64Encoder()).encode(raw);
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return hash;
}
