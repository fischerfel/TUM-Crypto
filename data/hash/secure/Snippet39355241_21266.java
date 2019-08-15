   public static String get_SHA_512_SecurePassword(String passwordToHash) throws Exception {
    String generatedPassword = null;

    MessageDigest md = MessageDigest.getInstance("SHA-512");
    byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    generatedPassword = sb.toString();
    System.out.println(generatedPassword);
    return generatedPassword;
}
