public static byte[] encrypt(String plainText, char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 2048);

    PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
    SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC", "BC");
    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

    Cipher encryptionCipher = Cipher.getInstance("PBEWithSHA256And256BitAES-CBC-BC", "BC");
    encryptionCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

    return encryptionCipher.doFinal(plainText.getBytes());
}
