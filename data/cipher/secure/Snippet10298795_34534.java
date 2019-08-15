private static byte[] encryptString(String message, Key publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] cipherData = cipher.doFinal(message.getBytes("UTF-8"));     
    return cipherData;
}
