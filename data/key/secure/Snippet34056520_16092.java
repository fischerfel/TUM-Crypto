public byte[] encrypt(byte[] key, byte[] input) throws Exception {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    SecureRandom secureRandom = new SecureRandom(key);
    secureRandom.setSeed(key);
    keyGenerator.init(128, secureRandom);
    SecretKey secretkey = keyGenerator.generateKey();
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding ");
    cipher.init(Cipher.ENCRYPT_MODE, secretkey);
    return cipher.doFinal(input);
}
public byte[] encrypt(byte[] key, byte[] input) throws Exception {
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES/CBC/NoPadding ");
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding ");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    return cipher.doFinal(input);
}
