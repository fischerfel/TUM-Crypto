public  String encrypt(String message) throws Exception
{
    String salt = SharedVariables.globalContext.getString(R.string.EncryptionKey);
    SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
    Cipher c = Cipher.getInstance("AES");
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(message.getBytes());
    String encrypted=Base64.encodeToString(encVal, Base64.DEFAULT);
    return encrypted;
}

public  String decrypt(String message) throws Exception
{
    String salt = SharedVariables.globalContext.getString(R.string.EncryptionKey);
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = Base64.decode(message.getBytes(), Base64.DEFAULT);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
}
