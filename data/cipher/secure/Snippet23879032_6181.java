private static String encryptString(String plainText, byte[] key, byte[] salt, byte[] iv) {
    try{
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        KeySpec keyspec = new PBEKeySpec(keyToCharArray(key), salt,
                getEncryptionKeyIterations(), 256);
        SecretKey secretKey = factory.generateSecret(keyspec);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey.getEncoded(), "AES")
                , new IvParameterSpec(iv));

        return Base64.encodeToString(cipher.doFinal(plainText.getBytes()), Base64.NO_WRAP);
    } catch(Exception ex) {
        Log.e(TAG, "Unable to encrypt message", ex);
        throw new InvalidDataException("Unable to encrypt message");
    }
}
