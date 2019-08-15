f    public static String encrypt(String sessionId)
{
    try
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        String key = "Abcdefghijklmnop";

        final SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
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
