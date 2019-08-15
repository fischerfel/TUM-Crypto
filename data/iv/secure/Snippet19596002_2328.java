public static final int IV_LENGTH = 16;
private static final String RANDOM_ALGORITHM = "SHA1PRNG";
...
    private static String translate(String clear, int mode) throws Exception {
    if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE)
        throw new IllegalArgumentException(
                "Encryption invalid. Mode should be either Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE");
    SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    IvParameterSpec ivSpec = new IvParameterSpec(generateIv());
    cipher.init(mode, skeySpec, ivSpec);
    byte[] encrypted = cipher.doFinal(clear.getBytes());
    return new String(encrypted);
}
...
    private static byte[] generateIv() throws NoSuchAlgorithmException,
        NoSuchProviderException {
    SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
    byte[] iv = new byte[IV_LENGTH];
    random.nextBytes(iv);
    return iv;
}
