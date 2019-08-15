public static byte[] generateAesSecretKey(){
    String SALT2 = "strong_salt_value";
    String username = "user_name";
    String password = "strong_password";
    byte[] key = (SALT2 + username + password).getBytes();
    SecretKey secretKeySpec = null;

    try {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKeySpec = new SecretKeySpec(key, "AES");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return secretKeySpec.getEncoded();
}
