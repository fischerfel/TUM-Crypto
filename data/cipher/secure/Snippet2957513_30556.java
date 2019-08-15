String password = "qwerty";
String plainText = "hello world";

byte[] salt = generateSalt();
byte[] cipherText = encrypt(plainText, password.toCharArray(), salt);

private static byte[] generateSalt() throws NoSuchAlgorithmException {
    byte salt[] = new byte[8];
    SecureRandom saltGen = SecureRandom.getInstance("SHA1PRNG");
    saltGen.nextBytes(salt);
    return salt;
}

private static byte[] encrypt(String plainText, char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    Security.addProvider(new BouncyCastleProvider());

    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);

    PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
    SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

    Cipher encryptionCipher = Cipher.getInstance("PBEWithSHA256And256BitAES-CBC-BC");
    encryptionCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

    return encryptionCipher.doFinal(plainText.getBytes());
}
