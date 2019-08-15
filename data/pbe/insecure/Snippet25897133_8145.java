public static byte[] decryptPrivateKey(byte[] key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    PBEKeySpec passKeySpec = new PBEKeySpec("p".toCharArray());

    EncryptedPrivateKeyInfo encryptedKey = new EncryptedPrivateKeyInfo(key);
    System.out.println(encryptedKey.getAlgName());
    System.out.println("key length: " + key.length);
    AlgorithmParameters algParams = encryptedKey.getAlgParameters();
    System.out.println(algParams.getAlgorithm());
