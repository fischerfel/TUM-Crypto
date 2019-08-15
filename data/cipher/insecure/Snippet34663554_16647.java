/** encrypt cipher */
private static final Cipher ENCRYPT_CIPHER = generateCipher(Cipher.ENCRYPT_MODE);

private static String ENCRYPT_KEY = "key";

/**
 * @param val
 * @return encrypted value
 * @throws Exception
 */
public String encrypt(final String val) throws Exception {
    return new String(Base64.encodeBase64(ENCRYPT_CIPHER.doFinal(val.getBytes()), true)).toString();
}

/**
 * @param encrypt
 * @return cipher
 */
protected static Cipher generateCipher(final int encrypt) {
    try {
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(encrypt, SecretKeyFactory.getInstance("AES").generateSecret(new IBMAESKeySpec(Base64.decodeBase64(ENCRYPT_KEY.getBytes()))));
        return cipher;
    } catch (final Exception e) {
        return null;
    }
}
