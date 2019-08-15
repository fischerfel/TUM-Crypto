private static byte[] appendIvToEncryptedData(byte[] eData, byte[] iv) throws Exception {
       ByteArrayOutputStream os = new ByteArrayOutputStream();
       os.write(eData);
       os.write(iv);
       return os.toByteArray();
    }

protected static byte[] dataEncryption(byte[] plainText)
    throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
    byte [] iv = new byte[Constants.AES_BYTE_LENGTH];
    random.nextBytes(iv);
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
    SecretKeySpec secretKeySpec = new SecretKeySpec(mAESKey, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
    return appendIvToEncryptedData(cipher.doFinal(plainText), cipher.getIV());
}


protected static byte[] dataDecryption(byte[] encrypted)
    throws Exception {
    int ivIndex = encrypted.length - Constants.AES_BYTE_LENGTH;
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec secretKeySpec = new SecretKeySpec(mAESKey, "AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, 
            new IvParameterSpec(encrypted, ivIndex, Constants.AES_BYTE_LENGTH));

    return cipher.doFinal(encrypted);
}
