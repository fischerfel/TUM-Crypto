public static String encryptPassword(String password) {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    if (md != null) {
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        String base64 = Base64.encodeToString(byteData, Base64.DEFAULT);

        return base64;
    }
    return password;
}
