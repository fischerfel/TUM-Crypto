private static byte[] key = {
        0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
};//"thisIsASecretKey";

public static byte[] EncryptByteArray(byte[] array)
{
    try
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return (cipher.doFinal(array));
    }
    catch (Exception e)
    {
      e.printStackTrace();

    }
    return null;
}

public static byte[] DecryptByteArray(byte[] array)
{
    try
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(array);
    }
    catch (Exception e)
    {
      e.printStackTrace();

    }
    return null;
}
