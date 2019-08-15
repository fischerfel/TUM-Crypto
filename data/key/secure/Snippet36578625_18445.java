public static String Encrypt(String Data) throws Exception 
{
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(Data.getBytes());
    String encryptedValue = new BASE64Encoder().encode(encVal); //Here is the problem

    return encryptedValue;
}

public static String Decrypt(String encryptedData) throws Exception 
{
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData); //Another problem
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);

    return decryptedValue;
}

private static Key generateKey() throws Exception 
{
    Key key = new SecretKeySpec(keyValue, ALGO);
    return key;
}
