public static String decrypt(String sessionId)
{
    try
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        final String decryptedSessionId = new String(cipher.doFinal(Base64.decodeBase64(sessionId)));
        return decryptedSessionId;
    }
    catch (Exception e)
    {
        e.printStackTrace();

    }
    return null;
}
