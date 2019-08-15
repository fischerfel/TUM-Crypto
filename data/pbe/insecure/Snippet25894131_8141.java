public static byte[] decryptPrivateKey(byte[] key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    PBEKeySpec passKeySpec = new PBEKeySpec("p".toCharArray()); //my password

    EncryptedPrivateKeyInfo encryptedKey = new EncryptedPrivateKeyInfo(key);
    System.out.println(encryptedKey.getAlgName());
    //PBEWithMD5AndDES
    System.out.println("key length: " + key.length);
    //key length: 677
    SecretKeyFactory keyFac = SecretKeyFactory.getInstance(encryptedKey.getAlgName());
    SecretKey passKey = keyFac.generateSecret(passKeySpec);

     // Create PBE Cipher
    Cipher pbeCipher = Cipher.getInstance(encryptedKey.getAlgName());
    // Initialize PBE Cipher with key and parameters
    pbeCipher.init(Cipher.DECRYPT_MODE, passKey, encryptedKey.getAlgParameters());

    // Decrypt the private key(throws the exception)
    return pbeCipher.doFinal(key);
}
