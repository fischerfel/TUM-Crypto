public static final String ALGORITHM = "PBEWithSHA1And256BitAES-CBC-BC";

public static byte[] encrypt(final byte[] key, final byte[] salt, final byte[] plainText) throws CryptoException {
    try {
        // Create the encryption key
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM, "BC");
        final PBEKeySpec keySpec = new PBEKeySpec(new String(key).toCharArray());
        final SecretKey secretKey = keyFactory.generateSecret(keySpec);

        // Encrypt the plain text
        final PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, ITERATIONS);
        final Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, cipherSpec);
        final byte[] encryptedBytes = cipher.doFinal(plainText);

        return encryptedBytes;

    } catch (final Throwable t) {
        throw new CryptoException(t.toString());
    }
}
