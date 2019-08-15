public static void main(String[] args) throws NoSuchAlgorithmException {
    System.out.println("encrypt:" + encryptPassword("superuser")+":" );
}

public static String encryptPassword(final String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] hashPassword = md.digest(password.getBytes());
    String encryPass = Base64.encodeBase64String(hashPassword);
    return encryPass;
}
