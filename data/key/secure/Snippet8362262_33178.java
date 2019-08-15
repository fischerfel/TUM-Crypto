protected static byte[] encrypt(byte[] data, String base64encodedKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher;
    try {
        cipher = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException ex) {
        //log error
    } catch (NoSuchPaddingException ex) {
        //log error
    }
    SecretKey key = b64EncodedStringToSecretKey(base64encodedKey);
    cipher.init(Cipher.ENCRYPT_MODE, key); //THIS IS WHERE IT FAILS
    data = cipher.doFinal(data);
    return data;
}
private static SecretKey b64EncodedStringToSecretKey(String base64encodedKey) {
    SecretKey key = null;

    try {
        byte[] temp = Base64.decodeBase64(base64encodedKey.getBytes());
        key = new SecretKeySpec(temp, SYMMETRIC_ALGORITHM);
    } catch (Exception e) {
        // Do nothing
    }

    return key;
}
