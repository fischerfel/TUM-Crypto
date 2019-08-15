public SecretKeySpec generateSecretKey(String password) throws Exception {
    MessageDigest shahash = MessageDigest.getInstance("SHA-1");
    byte[] key = shahash.digest(password.getBytes("UTF-8"));
    key = Arrays.copyOf(key,  16);
    return new SecretKeySpec(key, "AES");
}
