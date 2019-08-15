public static String decrypt(String strToDecrypt) throws UnsupportedEncodingException
{
     byte[] key = "12345678911234567891123456789112".getBytes("UTF-8");

    try
    {   Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt.getBytes("UTF-8"))));
        return decryptedString;
    }
    catch (Exception e)
    {
        System.out.println("Error while decrypting"+ e);

    }
    return null;
}
