public String decrypt(byte[] cipherText) throws Exception {

    String psk = "0123456789012345";
    String iv = "0123456789012345";
    try {
        String encryptionKey = psk;
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        final SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(UTF8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes(UTF8)));
        return new String(cipher.doFinal(cipherText), UTF8);
    } catch (BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException ex) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);
        throw new Exception(ex.getMessage());
    }
}
