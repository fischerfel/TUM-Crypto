private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
private static int KEY_LENGTH = 256;
...
public static byte[][] encrypt(byte[] plaintext, SecretKey key, byte[] salt) {
    try {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        byte[] iv = generateIv(cipher.getBlockSize());
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        byte[] cipherText = cipher.doFinal(plaintext);

        return new byte[][]{salt, iv, cipherText};

    } catch (GeneralSecurityException e) {
        throw new RuntimeException(e);
    }
}
