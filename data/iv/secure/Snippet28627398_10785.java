public static byte[] getEncryptedPassword(String password, byte[] salt,
        int iterations, int derivedKeyLength)
        throws NoSuchAlgorithmException, InvalidKeySpecException {

    KeySpec mKeySpec = new PBEKeySpec(password.toCharArray(), salt,
            iterations, derivedKeyLength);

    SecretKeyFactory mSecretKeyFactory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA256");

    return mSecretKeyFactory.generateSecret(mKeySpec).getEncoded();

}

public String encrypt(String dataToEncrypt, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

    byte[] mEncryptedPassword = getEncryptedPassword(key, generateSalt(),
            16384, 128);

    SecretKeySpec mSecretKeySpec = new SecretKeySpec(mEncryptedPassword, "AES");


    Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    mCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec);

    byte[] ivBytes = mCipher.getIV();
    byte[] encryptedTextBytes = mCipher.doFinal(dataToEncrypt.getBytes());

    byte[] combined = new byte[ivBytes.length+encryptedTextBytes.length];       
    System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
    System.arraycopy(encryptedTextBytes, 0, combined, ivBytes.length, encryptedTextBytes.length);

    return Base64.getEncoder().encodeToString(combined);

}

public String decrypt(String dataToDecrypt, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {


    byte[] encryptedCombinedBytes = Base64.getDecoder().decode(dataToDecrypt);
    byte[] mEncryptedPassword = getEncryptedPassword(key, generateSalt(),
            16384, 128);

    byte[] ivbytes = Arrays.copyOfRange(encryptedCombinedBytes,0,16);

    SecretKeySpec mSecretKeySpec = new SecretKeySpec(mEncryptedPassword, "AES");

    Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    mCipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec, new IvParameterSpec(ivbytes));    

    byte[] encryptedTextBytes = Arrays.copyOfRange(encryptedCombinedBytes, 16, encryptedCombinedBytes.length);

    System.out.println(encryptedTextBytes.length);
    byte[] decryptedTextBytes = mCipher.doFinal(encryptedTextBytes);



    return Base64.getEncoder().encodeToString(decryptedTextBytes);

}

public byte[] generateSalt() {
    SecureRandom random = new SecureRandom();
    byte saltBytes[] = new byte[16];
    random.nextBytes(saltBytes);
    return saltBytes;
}
