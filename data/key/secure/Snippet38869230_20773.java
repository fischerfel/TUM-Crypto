public String encryptWithAES(String value, String key, String encoding) throws NoSuchAlgorithmException, NoSuchPaddingException, DecoderException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] keyBytes = Hex.decodeHex(key.toLowerCase().toCharArray());
    byte[] dataToSend = value.getBytes(encoding);
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(keyBytes, "AES");
    c.init(1, k);
    byte[] encryptedData = c.doFinal(dataToSend);
    return new String(Hex.encodeHex(encryptedData));
}

public String decryptAES(String encrypted, String key, String encoding) throws NoSuchAlgorithmException, NoSuchPaddingException, DecoderException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] keyBytes = Hex.decodeHex(key.toCharArray());
    byte[] encryptedData = Hex.decodeHex(encrypted.toCharArray());
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(keyBytes, "AES");
    c.init(2, k);
    byte[] dencryptedData = c.doFinal(encryptedData);
    return new String(dencryptedData, encoding);
}
