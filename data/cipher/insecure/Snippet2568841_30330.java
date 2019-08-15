public static SecretKey getSecretKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    // NOTE: last argument is the key length, and it is 256
    KeySpec spec = new PBEKeySpec(password, salt, 1024, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    return(secret);
}


public static byte[] encrypt(char[] password, byte[] salt, String text) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
    SecretKey secret = getSecretKey(password, salt);

    Cipher cipher = Cipher.getInstance("AES");

    // NOTE: This is where the Exception is being thrown
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    byte[] ciphertext = cipher.doFinal(text.getBytes("UTF-8"));
    return(ciphertext);
}
