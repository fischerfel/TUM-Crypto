public static String passwordencrypt(String password) 
        throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());
    byte[] b = md.digest();
    return base64(b);
}

public static String base64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
}
