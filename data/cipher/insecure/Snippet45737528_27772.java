    public static String encrypt(String sessionId)
{
    try
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        final String encryptedSessionId = Base64.encodeBase64String(cipher.doFinal(sessionId.getBytes()));
        return encryptedSessionId;
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return null;

}
