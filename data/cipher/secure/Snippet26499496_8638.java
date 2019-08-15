public static String encryptString(String clearText, byte[] key, byte[] initialVector) throws Exception
{
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec secretKeySpecy = new SecretKeySpec(key, "AES");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpecy, ivParameterSpec);
    byte[] encrypted = cipher.doFinal(clearText.getBytes());
    return new String(Base64.encodeBase64(encrypted, false));
}

public static String decryptString(String cipherText, byte[] key, byte[] initialVector) throws Exception
{
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec secretKeySpecy = new SecretKeySpec(key, "AES");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
    return new String(cipher.doFinal(Base64.decodeBase64(cipherText)));
}
