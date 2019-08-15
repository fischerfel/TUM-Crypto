  public static byte[] encrypt(byte[] data) {
    try {
        KeyPair keyPair = initalizeKeyPair();

        final javax.crypto.Cipher rsa = javax.crypto.Cipher.getInstance("RSA");
        rsa.init(javax.crypto.Cipher.ENCRYPT_MODE, keyPair.getPublic());

        SecureRandom random = new SecureRandom();

        final byte[] secretKey = new byte[16];
        random.nextBytes(secretKey);

        final javax.crypto.Cipher aes = javax.crypto.Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(secretKey, "AES");
        aes.init(javax.crypto.Cipher.ENCRYPT_MODE, k);

        final byte[] ciphedKey = rsa.doFinal(secretKey);
        final byte[] ciphedData = aes.doFinal(data);

        byte[] result = new byte[256 + ciphedData.length];

        System.arraycopy(ciphedKey, 0, result, 0, 256);
        System.arraycopy(ciphedData, 0, result, 256, ciphedData.length);

        return result;
    } catch (... e) {
        throw new SomeException(e);
    }
}
