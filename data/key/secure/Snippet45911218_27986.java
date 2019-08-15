private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] iv = new byte[cipher.getBlockSize()];
    IvParameterSpec ivParams = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivParams);
    return cipher.doFinal(data);
}

private static byte[] decrypt(byte[] encrypted, byte[] key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] ivByte = new byte[cipher.getBlockSize()];
    IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), ivParamsSpec);
    return cipher.doFinal(encrypted);
}
