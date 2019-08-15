public static byte[] encryptAES256(byte[] data, String passphrase, byte[] salt, int iterations, byte[] ivbytes) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec specKey = new PBEKeySpec(passphrase.toCharArray(), salt, iterations, 256);
    SecretKey secretKey = factory.generateSecret(specKey);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivbytes));
    return cipher.doFinal(data);
}

public static byte[] decryptAES256(byte[] data, String passphrase, byte[] salt, int iterations, byte[] ivbytes) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec specKey = new PBEKeySpec(passphrase.toCharArray(), salt, iterations, 256);
    SecretKey secretKey = factory.generateSecret(specKey);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivbytes));
    return cipher.doFinal(data);
}
