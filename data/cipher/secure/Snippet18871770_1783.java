private String getDecryptedString(byte[] credentials, PrivateKey secretKey) throws NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        String decryptedString;
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "SunJCE");
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] messageDecrypte = decryptCipher.doFinal(credentials);
        decryptedString = new String(messageDecrypte);
        return decryptedString;
    }
