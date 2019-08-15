protected static final String DES_ECB_PKCS5PADDING = "DESede/ECB/PKCS5Padding";

 public static String decryptValueDirect(String value, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            GeneralSecurityException, IllegalBlockSizeException,
            BadPaddingException {
        byte[] bytes = Base64.decodeBase64(value);
        Cipher cipher = Cipher.getInstance(DES_ECB_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, convertSecretKey(key.getBytes()));
        byte[] decryptedValue = cipher.doFinal(bytes);

        String nstr =  new String(decryptedValue);
        return nstr;
    }
protected static SecretKey convertSecretKey(byte[] encryptionKey) throws GeneralSecurityException {
        if (encryptionKey == null || encryptionKey.length == 0)
            throw new IllegalArgumentException("Encryption key must be specified");

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(TRIPLEDES);
        KeySpec keySpec = new DESedeKeySpec(encryptionKey);
        return keyFactory.generateSecret(keySpec);
    }
