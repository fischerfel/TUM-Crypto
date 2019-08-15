public static String encrypt(String strToEncrypt)
{
    try
    {
        String secretKey = "1234567890123456";
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        BASE64Encoder base64encoder = new BASE64Encoder();
        byte[] cleartext = strToEncrypt.getBytes("UTF8");
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        String encrypted = base64encoder.encode(cipher.doFinal(cleartext));
        return encrypted;

    }
    catch (Exception e)
    {
        return e.getMessage();
    }
}
