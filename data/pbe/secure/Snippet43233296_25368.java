private byte[] getRandomNumber(final int size) throws NoSuchAlgorithmException {
    SecureRandom secureRandom = SecureRandom.getInstanceStrong();
    byte[] randomBytes = new byte[size];
    secureRandom.nextBytes(randomBytes);
    return randomBytes;
}


private SecretKey getPBE_AES_Key(final String password, final byte[] salt) {
    try {
        char[] passwdData = password.toCharArray();

        PBEKeySpec pbeKeySpec = new PBEKeySpec(passwdData, salt, 4096, 128);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
        SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
        return pbeKey; // <-- size of this byte array is 9 - I thought it should be 16 since its AES 
    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
        throw new OperationFailedException(ex.getMessage(), ex);
    }
}


public String encrypt_PBE_AES(final String plaintext, final String password) {
    try {
        byte[] ivBytes = getRandomNumber(16);
        byte[] saltBytes = getRandomNumber(16);
        byte[] dataToEncrypt = plaintext.getBytes("UTF-8");

        Cipher cipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(saltBytes, 4096, ivParameterSpec);

        cipher.init(Cipher.ENCRYPT_MODE, getPBE_AES_Key(password, saltBytes), pbeParameterSpec);
        byte[] encryptedData = cipher.doFinal(dataToEncrypt);

        byte[] ivWithSalt = ArrayUtils.addAll(ivBytes, saltBytes);
        byte[] encryptedDataWithIVAndSalt = ArrayUtils.addAll(ivWithSalt, encryptedData);
        String encodedData = Base64.getUrlEncoder().encodeToString(encryptedDataWithIVAndSalt);
        return encodedData;
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
        | BadPaddingException | IOException | InvalidAlgorithmParameterException ex) {
        throw new OperationFailedException(ex.getMessage(), ex);
    }
}

public String decrypt_PBE_AES(final String ciphertext, final String password) {
    try {
        byte[] encryptedDataWithIVAndSalt = Base64.getUrlDecoder().decode(ciphertext);
        byte[] ivBytes = ArrayUtils.subarray(encryptedDataWithIVAndSalt, 0, 16);
        byte[] saltBytes = ArrayUtils.subarray(encryptedDataWithIVAndSalt, 16,
            16 + 16);
        byte[] dataToDecrypt = ArrayUtils.subarray(encryptedDataWithIVAndSalt,
            16 + 16, encryptedDataWithIVAndSalt.length);

        Cipher cipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(saltBytes, 4096, ivParameterSpec);

        cipher.init(Cipher.DECRYPT_MODE, getPBE_AES_Key(password, saltBytes), pbeParameterSpec);
        byte[] decryptedData = cipher.doFinal(dataToDecrypt);

        return new String(decryptedData, "UTF-8");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException
        | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException ex) {
        throw new OperationFailedException(ex.getMessage(), ex);
    }
}
