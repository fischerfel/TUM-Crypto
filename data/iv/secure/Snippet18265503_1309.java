public  String decrypt(byte[] cipherText, SecretKey key, byte [] initialVector) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
    cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
    cipherText = cipher.doFinal(cipherText);

    return new String(cipherText, "UTF-8");
}
