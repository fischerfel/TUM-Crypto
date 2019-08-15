private static final String AES_CIPHER_METHOD = "AES";

public static SecretKeySpec createAesKeySpec(byte[] aesKey) {
    return new SecretKeySpec(aesKey, AES_CIPHER_METHOD);
}

public static String aesEncrypt(String data, SecretKeySpec aesKeySpec) throws EncryptionException {
    try {
        Cipher aesCipher = Cipher.getInstance(AES_CIPHER_METHOD);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);
        byte[] encVal = aesCipher.doFinal(data.getBytes("UTF8"));
        return new BASE64Encoder().encode(encVal);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | BadPaddingException| IllegalBlockSizeException e) {
        throw new EncryptionException(e.getMessage(), e);
    }
}

public static void aesEncryptFile(File in, File out, SecretKeySpec aesKeySpec) throws EncryptionException {
    try {
        Cipher aesCipher = Cipher.getInstance(AES_CIPHER_METHOD);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);

        try (InputStream inputStream = new FileInputStream(in)) {
            try (OutputStream outputStream = new CipherOutputStream(new FileOutputStream(out), aesCipher)){
                IOUtils.copy(inputStream, outputStream);
            }
        }
    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e){
        throw new EncryptionException(e.getMessage(), e);
    }
}
