public byte[] uncTripleDes (byte [] encryptedTextBytes, byte [] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException{
    DESedeKeySpec keySpec = new DESedeKeySpec(key);
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
    SecretKey ky = keyfactory.generateSecret(keySpec);

    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, ky);
    return cipher.doFinal(encryptedTextBytes);

}
