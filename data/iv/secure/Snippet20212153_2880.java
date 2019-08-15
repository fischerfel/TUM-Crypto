public static String[] decrypt(String[] strToDecrypt)
{
    String decryptedString[] = new String[strToDecrypt.length];
    for(int i = 0;i<strToDecrypt.length;i++) {
    try
    {
        //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        Cipher cipher = Cipher.getInstance("aes/cbc/nopadding");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ips = new IvParameterSpec(iv);
        //cipher.init(Cipher.DECRYPT_MODE, secretKey);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,ips);
        decryptedString[i] = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt[i])));

    }
    catch (Exception e)
    {
        log.error("Error while decrypting : " + strToDecrypt[i] , e);
    }
    }return decryptedString;
}
