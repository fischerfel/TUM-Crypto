public static String encryptText(String plainText, String key) throws Exception {
    char[] password = key.toCharArray();
    byte[] salt = getNextSalt();
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE, secret);
    byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
    return Base64.getEncoder().encodeToString(byteCipherText);

}

public static String decryptText(String encryptString, String key) throws Exception {
    char[] password = key.toCharArray();
    byte[] salt = getNextSalt();
    SecretKeyFactory factory1 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
    SecretKey tmp = factory1.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    byte[] byteCipherText = Base64.getDecoder().decode(encryptString);
    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.DECRYPT_MODE, secret);
    byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
    return new String(bytePlainText);
}

private static byte[] getNextSalt() {
    return "abcdefgh".getBytes();
}
