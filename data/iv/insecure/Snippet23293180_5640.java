private final static String passPhrase = "My Super Ultra Passphrase";
private final static byte[] salt = "My Super Ultra Salt".getBytes();
private final static int iterations = 8192;
private static String strIv = "";

private static String encryptText(String text) {
    String result = "";
    try {
        // create the key for encryption
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secret = factory.generateSecret(
                new PBEKeySpec(passPhrase.toCharArray(), salt, iterations, 128));
        SecretKeySpec key = new SecretKeySpec(secret.getEncoded(), "AES");

        // encrypts the text
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aes.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = aes.doFinal(text.getBytes());
        byte[] iv = aes.getIV();

        result = new String(Base64.encode(cipherText));
        strIv = new String(Base64.encode(iv));

    } catch (NoSuchAlgorithmException 
            | InvalidKeySpecException 
            | NoSuchPaddingException 
            | InvalidKeyException 
            | IllegalBlockSizeException 
            | BadPaddingException e) {
        e.printStackTrace();
    }
    return result;
}

private static String decryptText(String text) {
    String result = "";
    try {
        // create the key for decryption
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secret = factory.generateSecret(
                new PBEKeySpec(passPhrase.toCharArray(), salt, iterations, 128));
        SecretKeySpec key = new SecretKeySpec(secret.getEncoded(), "AES");

        byte[] iv = Base64.decode(strIv);
        byte[] cipherText = Base64.decode(text);

        // decrypt the text
        Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aes.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        result = new String(aes.doFinal(cipherText));

    } catch (NoSuchAlgorithmException 
            | InvalidKeySpecException 
            | NoSuchPaddingException 
            | InvalidKeyException 
            | IllegalBlockSizeException 
            | BadPaddingException 
            | InvalidAlgorithmParameterException
            | Base64DecodingException e) {
        e.printStackTrace();
    }
    return result;
}
