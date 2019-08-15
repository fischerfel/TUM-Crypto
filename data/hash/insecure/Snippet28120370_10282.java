public static String Cripto(String Password)
{
    String PasswordCripto = "";
    try
    {
        String encryptionKey = "anyEncryptionString";
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(encryptionKey.getBytes("UTF-8"), 0, encryptionKey.length());
        byte[] encryptionKeyBytes = messageDigest.digest();

        SecretKeySpec Key = new SecretKeySpec(encryptionKeyBytes,"DESede");
        Cipher cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, Key);
        byte[] encryptedBytes = cipher.doFinal(Password.getBytes("UTF-8"));

        PasswordCripto = new String(Base64.encode(encryptedBytes, Base64.DEFAULT), "UTF-8");
    } catch(Exception e) { }
    return PasswordCripto ;
}
