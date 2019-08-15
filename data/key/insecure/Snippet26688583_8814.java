private final static String algorithm = "AES";
private final static byte[] keyValue = new byte[]      {'@', 'j', 'a', 'z', 'p', 'a', 'w', 'm', 'd', 'n', 'c', '5', 'y', 'p', 't', '*'};

// generates a secret key
 private static Key generateKey( ) throws Exception {
    Key key = new SecretKeySpec(keyValue, algorithm);
    return key;
}

// Performs Encryption
public static String encrypt(String plainText) throws Exception {
    Key key = generateKey();
    Cipher chiper = Cipher.getInstance(algorithm);
    chiper.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = chiper.doFinal(plainText.getBytes());
    return new BASE64Encoder().encode(encVal);
}

// Performs decryption
public static String decrypt(String encryptedText) throws Exception {
    // generate key 
    Key key = generateKey();
    Cipher chiper = Cipher.getInstance(algorithm);
    chiper.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
    byte[] decValue = chiper.doFinal(decordedValue);
    return new String(decValue);
}
