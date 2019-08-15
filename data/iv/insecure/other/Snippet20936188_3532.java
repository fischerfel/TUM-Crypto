public static SecretKey getSecretKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    // NOTE: last argument is the key length, and it is 128
    KeySpec spec = new PBEKeySpec(password, salt, 1024, 128);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    return(secret);
}

public static byte[] encrypt(char[] password, byte[] salt, String text) throws GeneralSecurityException {
    SecretKey secret = getSecretKey(password, salt);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[cipher.getBlockSize()]));
    byte[] ciphertext = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
    return(ciphertext);
}
