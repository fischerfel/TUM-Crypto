public static String decrypt(String data, PrivateKey privateKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
    String[] parts = data.split("~");

    // Decrypt AES secret key
    byte[] encodedSecretKey = Base64.getDecoder().decode(parts[0]);
    Cipher rsa = Cipher.getInstance("RSA");
    rsa.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decodedSecretKey = rsa.doFinal(encodedSecretKey);
    SecretKeySpec key = new SecretKeySpec(decodedSecretKey, "AES");

    // Decrypt message
    Cipher aes = Cipher.getInstance("AES");
    aes.init(Cipher.DECRYPT_MODE, key);
    byte[] decodedData = aes.doFinal(Base64.getDecoder().decode(parts[1]));

    return new String(decodedData);
}
