private static byte[] decrypt(SecretKey key, byte[] encrypted, byte[] iv) throws Exception {
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    Cipher cipher = Cipher.getInstance("AES/CCM/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}

public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    // Number of PBKDF2 hardening rounds to use. Larger values increase
    // computation time. You should select a value that causes computation
    // to take >100ms.
    final int iterations = 1000;

    // Generate a 128-bit key
    final int outputKeyLength = 128;

    /*SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);*/

    PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator(new SHA256Digest());
    generator.init(PBEParametersGenerator.PKCS5PasswordToBytes(passphraseOrPin), salt, iterations);
    KeyParameter key = (KeyParameter) generator.generateDerivedMacParameters(outputKeyLength);
    SecretKey secretKey = new SecretKeySpec(key.getKey(), "AES");
    return secretKey;
}
