private static byte[] getPasswordMessageDigest(String password) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
    byte[] passwordMessageDigest = messageDigest.digest(password.getBytes());

    return passwordMessageDigest;
}

public static SecretKey createSecretKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] passwordMessageDigest = getPasswordMessageDigest(password);

    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    secureRandom.setSeed(passwordMessageDigest);

    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128, secureRandom);

    SecretKey secretKey = keyGenerator.generateKey();

    return secretKey;
}

public static byte[] encrypt(String password, byte[] plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance("AES");

    SecretKey secretKey = createSecretKey(password);

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    byte[] cipherText = cipher.doFinal(plainText);

    return cipherText;
}

public static byte[] decrypt(String password, byte[] cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance("AES");

    SecretKey secretKey = createSecretKey(password);

    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    byte[] plainText = cipher.doFinal(cipherText);

    return plainText;
}
