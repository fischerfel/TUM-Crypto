public static byte[] decrypt() throws Exception
{
    try{
        byte[] ciphertextBytes = convertToBytes("cb12f5ca1bae224ad44fdff6e66f9a53e25f1000183ba5568958430c11c6eafc62c04de8bf27e0ac7104b598fb492142");
        byte[] keyBytes = convertToBytes("CFDC65CB003DD50FF5D6D826D62CF9CA6C64489D60CB02D18C1B58C636F8220D");
        byte[] ivBytes = convertToBytes("cb12f5ca1bae224a");

        SecretKey aesKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(ivBytes));

        byte[] result = cipher.doFinal(ciphertextBytes);
        return result;
    }
    catch(Exception e)
    {
        System.out.println(e.getMessage());
    }
     return null;
}
