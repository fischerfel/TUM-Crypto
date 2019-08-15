public static byte[] encrypt(byte[] plaintext) {
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey key = new SecretKeySpec(hexStringToByteArray(klucz2), "AES");

        SecureRandom random = new SecureRandom();
        byte iv[] = new byte[16];//generate random 16 byte IV AES is always 16bytes
        random.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
        byte[] encrypted = cipher.doFinal(plaintext);
        byte[] ciphertext = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, ciphertext, 0, iv.length);
        System.arraycopy(encrypted, 0, ciphertext, iv.length, encrypted.length);
        return ciphertext;

    } catch (InvalidKeyException | NoSuchAlgorithmException
            | NoSuchPaddingException
            | IllegalBlockSizeException | InvalidAlgorithmParameterException
            | BadPaddingException e) {
        throw new IllegalStateException(
                "CBC encryption with standard algorithm should never fail",
                e);
    }
} 
