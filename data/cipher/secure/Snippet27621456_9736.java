/**
 * Creates a cipher for encryption or decryption.
 * 
 * @param algorithm  PBE algorithm like "PBEWithMD5AndDES" or "PBEWithMD5AndTripleDES".
 * @param mode Encyrption or decyrption.
 * @param password Password
 * @param salt Salt usable with algorithm.
 * @param count Iterations.
 * @return Ready initialized cipher.
 * @throws GeneralSecurityException Error creating the cipher.
 */
private static Cipher createCipher(final String algorithm, final int mode, final char[] password, final byte[] salt, final int count) throws GeneralSecurityException {
    final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
    final PBEKeySpec keySpec = new PBEKeySpec(password);
    final SecretKey key = keyFactory.generateSecret(keySpec);
    final Cipher cipher = Cipher.getInstance(algorithm);
    final PBEParameterSpec params = new PBEParameterSpec(salt, count);
    cipher.init(mode, key, params);
    return cipher;
}

/**
 * Encrypts some data based on a password.
 * @param algorithm PBE algorithm like "PBEWithMD5AndDES" or "PBEWithMD5AndTripleDES"
 * @param data Data to encrypt
 * @param password Password
 * @param salt Salt usable with algorithm
 * @param count Iterations.
 * @return Encrypted data.
 */
public static byte[] encryptPasswordBased(final String algorithm, final byte[] data, final char[] password, final byte[] salt, final int count) {
    Validate.notNull(algorithm);
    Validate.notNull(data);
    Validate.notNull(password);
    Validate.notNull(salt);
    try {
        final Cipher cipher = createCipher(algorithm, Cipher.ENCRYPT_MODE, password, salt, count);
        return cipher.doFinal(data);
    } catch (final Exception ex) {
        throw new RuntimeException("Error encrypting the password!", ex);
    }
}

/**
 * Decrypts some data based on a password.
 * @param algorithm PBE algorithm like "PBEWithMD5AndDES" or "PBEWithMD5AndTripleDES"
 * @param encryptedData Data to decrypt
 * @param password Password
 * @param salt Salt usable with algorithm
 * @param count Iterations.
 * @return Encrypted data.
 */
public static byte[] decryptPasswordBased(final String algorithm, final byte[] encryptedData, final char[] password, final byte[] salt, final int count) {
    Validate.notNull(algorithm);
    Validate.notNull(encryptedData);
    Validate.notNull(password);
    Validate.notNull(salt);
    try {
        final Cipher cipher = createCipher(algorithm, Cipher.DECRYPT_MODE, password, salt, count);
        return cipher.doFinal(encryptedData);
    } catch (final Exception ex) {
        throw new RuntimeException("Error decrypting the password!", ex);
    }
}
