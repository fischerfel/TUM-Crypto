public byte[] deriveKey(String password, byte[] salt, int keyLen) {
    SecretKeyFactory kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec specs = new PBEKeySpec(password.toCharArray(), salt, 1024, keyLen);
    SecretKey key = kf.generateSecret(specs);
    return key.getEncoded();
}
