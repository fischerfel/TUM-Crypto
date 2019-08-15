public static String encryptWithPublicKey(byte[] message, PublicKey publicKey) throws Exception {
    PublicKey apiPublicKey = publicKey;
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    rsaCipher.init(Cipher.ENCRYPT_MODE, apiPublicKey);
    byte[] encVal = rsaCipher.doFinal(message);
    String encryptedValue = new BASE64Encoder().encode(encVal);
    return encryptedValue;
}

public static String decryptWithPrivateKey(byte[] message, PrivateKey privateKey) throws Exception {
    PrivateKey pKey = privateKey;
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    rsaCipher.init(Cipher.DECRYPT_MODE, pKey);
    byte[] decVal = rsaCipher.doFinal(message);
    String decryptedValue = new String(decVal);
    return decryptedValue;
}
