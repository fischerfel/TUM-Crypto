public static byte[] decrypt(byte[] ivBytes, byte[] keyBytes,
            byte[] textBytes) throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        // SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(keyBytes, "AES"), ivSpec);
        return cipher.doFinal(textBytes);
    }
