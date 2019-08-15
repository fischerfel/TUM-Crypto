   public  byte[] decrypt(byte[] cipherText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
{
    Cipher cipher = Cipher.getInstance(cipherTransformation);
    SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
    cipherText = cipher.doFinal(cipherText);
    return cipherText;
}

public byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
{
    Cipher cipher = Cipher.getInstance(cipherTransformation);
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
    plainText = cipher.doFinal(plainText);
    return plainText;
}
