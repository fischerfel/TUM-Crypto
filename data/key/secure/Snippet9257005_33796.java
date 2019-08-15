public static byte[] encrypt(byte[] bytes, byte[] key, byte[] iv)
        throws Exception
{
    Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"),
            new IvParameterSpec(iv));
    return cipher.doFinal(bytes);
}
