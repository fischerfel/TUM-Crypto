public static byte[] enDeCrypt(byte[] data, SecretKey secretKey,
        byte[] initialisationVector) {

    try {
        IvParameterSpec ivSpec = new IvParameterSpec(initialisationVector);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(data);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;

    } catch (NoSuchAlgorithmException | NoSuchPaddingException
            | InvalidKeyException | InvalidAlgorithmParameterException
            | IllegalBlockSizeException | BadPaddingException e) {
        throw new RuntimeException(e);
    }

}
