public static byte[] decrypt(byte[] input)
{
    try
    {
        SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey);
        return cipher.doFinal(input);
    }
    catch (Exception e) {}
    return input;
}
