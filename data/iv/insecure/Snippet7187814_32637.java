public String encryptText(String cipherText) throws Exception {

    String plainKey = "key12345";
    String plainIV = "1234567890ABCDEF";

    KeySpec ks = new  DESKeySpec(plainKey.getBytes(encodingType));
    SecretKey key = SecretKeyFactory.getInstance(keyDes).generateSecret(ks);

    IvParameterSpec iv = new IvParameterSpec(
            org.apache.commons.codec.binary.Hex.decodeHex(plainIV.toCharArray()));

    Cipher cipher = Cipher.getInstance(encryptAlgo);
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    byte[] decoded = cipher.doFinal(cipherText.getBytes(encodingType));

    return new Base64().encodeToString(decoded);
}
