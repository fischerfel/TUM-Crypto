private static final String ALGORITHM = "AES";

 protected static String encrypt(String valueToEnc, byte[] keyValue) throws Exception {
    Key key = generateKey(keyValue);
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encValue = c.doFinal(valueToEnc.getBytes());
    String encryptedValue = new BASE64Encoder().encode(encValue);
    return encryptedValue;
}

protected static String decrypt(String encryptedValue, byte[] keyValue) throws Exception {
    try
    {
        Key key = generateKey(keyValue);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    catch(Exception ex)
    {
        return "";
    }
}

private static Key generateKey(byte[] keyValue) throws Exception {          
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    return key;
}
