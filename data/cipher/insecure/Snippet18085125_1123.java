public static String encrypt(byte[] key, byte[] iv, String unencrypted) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException{
    RC2ParameterSpec ivSpec = new RC2ParameterSpec(key.length*8, iv);
    Cipher cipher = Cipher.getInstance("RC2/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "RC2"), ivSpec);
    byte[] encrypted = cipher.doFinal(unencrypted.getBytes());
    return DatatypeConverter.printBase64Binary(encrypted);
}

public static String decrypt(byte[] key, byte[] iv, String encrypted) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException{
    RC2ParameterSpec ivSpec = new RC2ParameterSpec(key.length*8, iv);
    Cipher cipher = Cipher.getInstance("RC2/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "RC2"), ivSpec);
    byte[] decrypted = cipher.doFinal(DatatypeConverter.parseBase64Binary(encrypted));

    return new String(decrypted);
}
