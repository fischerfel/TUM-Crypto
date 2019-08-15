public static String encrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
    Cipher cipher = Cipher.getInstance(ENC_ALG);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALG);

    KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, 256);
    SecretKey key = keyFactory.generateSecret(keySpec);

    IvParameterSpec ivSpec = new IvParameterSpec(IV);    
    PBEParameterSpec paramSpec = new PBEParameterSpec(SALT, 0,ivSpec);

    cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
    byte[] encrypted = cipher.doFinal(text.getBytes());

    return Base64.getEncoder().encodeToString(encrypted);
}

public static String decrypt(String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    Cipher cipher = Cipher.getInstance(ENC_ALG);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALG);

    KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, 256);
    SecretKey key = keyFactory.generateSecret(keySpec);
    IvParameterSpec ivSpec = new IvParameterSpec(IV);    
    PBEParameterSpec paramSpec = new PBEParameterSpec(SALT, 0, ivSpec);
    cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    byte[] decoded = Base64.getDecoder().decode(encrypted);
    byte[] decrypted = cipher.doFinal(decoded);
    return new String(decrypted);
}
