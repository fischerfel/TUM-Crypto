public byte[] deriveKey(String password, byte[] salt, int keyLen) {
    SecretKeyFactory kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec specs = new PBEKeySpec(password.toCharArray(), salt, 1024, keyLen);
    SecretKey key = kf.generateSecret(specs);
    return key.getEncoded();
}

public byte[] encrypt(String password, byte[] plaintext) {
    byte[] salt = new byte[64];
    Random rnd = new Random();
    rnd.nextByte(salt);
    byte[] data = deriveKey(password, salt, 192);
    SecretKey desKey = SecretKeyFactory.getInstance("DESede").generateSecret(new DESedeKeySpec(data));
    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, desKey);
    return cipher.doFinal(plaintext);
}
