 public static String encrypt(String plainText) throws Exception 
{
        Key key = generateKey();
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = chiper.doFinal(plainText.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
}

public static String decrypt(String encryptedText) throws Exception 
{
    // generate key 
    Key key = generateKey();
    Cipher chiper = Cipher.getInstance(algorithm);
    chiper.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = new     BASE64Decoder().decodeBuffer(encryptedText);
    byte[] decValue = chiper.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
}
