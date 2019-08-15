 public static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
 public static String wrap(String clearText, String key) {
    byte[] iv = getIv();

    byte[] cipherText = encrypt(clearText, key, iv);
    byte[] wrapped = new byte[iv.length + cipherText.length];
    System.arraycopy(iv, 0, wrapped, 0, iv.length);
    System.arraycopy(cipherText, 0, wrapped, 16, cipherText.length);

    return new String(Base64.encodeBase64(wrapped));
}

private static byte[] encrypt(String clearText, String key, byte[] iv) {
    try {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        cipher.init(Cipher.ENCRYPT_MODE, getKey(key), params);
        return cipher.doFinal(clearText.getBytes());
    } catch (GeneralSecurityException e) {
        throw new RuntimeException("Failed to encrypt.", e);
    }
}

private static SecretKeySpec getKey(String key) {
    try {
        return new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "AES");
    } catch (DecoderException e) {
        throw new RuntimeException("Failed to generate a secret key spec", e);
    }
}

private static byte[] getIv() {
    byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);

    return iv;
}
