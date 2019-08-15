    private static final String ALGORITHM = "AES";
    ....
    ....
    Key key = new SecretKeySpec(new String("here is your symmetric key").getBytes(), ALGORITHM);
    Cipher c = Cipher.getInstance(ALGORITHM);
    //dencript mode (passes the key)
    c.init(Cipher.DECRYPT_MODE, key);
    //Decode base64 to get bytes 
    byte[] encBytes  = new BASE64Decoder().decodeBuffer(encryptedValue);
    // Decrypt 
    byte[] plainTxtBytes  = c.doFinal(encBytes);
    // Decode
    String decryptedValue = new String(plainTxtBytes , "UTF-8");
