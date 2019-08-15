public static SecretKey generateKeyFromPassword(String password, byte[] salt) throws GeneralSecurityException {
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 1000, 256);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
    return new SecretKeySpec(keyBytes, "AES");
}
