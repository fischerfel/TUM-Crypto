public static byte[] encrypt(String plainText, char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC", "BC");
    KeySpec spec = new PBEKeySpec(password, salt, 2048, 256);
    SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher.doFinal(plainText.getBytes());
}
