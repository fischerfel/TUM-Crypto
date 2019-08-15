public String decrypt(CryptGroup cp) throws Exception {
    String plaintext = null;
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password, cp.getSalt(), ITERATIONS, KEY_SIZE);
    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(cp.getIv()));
    plaintext = new String(cipher.doFinal(cp.getCipher()), "UTF-8");

    return plaintext;
}

public CryptGroup encrypt(String plainText) throws Exception {
    byte[] salt = generateSalt();
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_SIZE);
    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] ciphertext = cipher.doFinal(plainText.getBytes("UTF-8"));

    return new CryptGroup(ciphertext, salt, iv);
}
