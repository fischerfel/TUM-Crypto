private byte[] decryptSecretKeyData(byte[] encryptedSecretKey, byte[] iv) throws Exception {
    try {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_NOPADDING, providerPKCS11);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] paddedPlainText = cipher.doFinal(encryptedSecretKey);
        System.out.println("OAEP padded plain text: " + Arrays.toString(paddedPlainText));
        if (paddedPlainText.length < keyLength / 8) {
            byte[] tmp = new byte[keyLength / 8];
            System.arraycopy(paddedPlainText, 0, tmp, tmp.length - paddedPlainText.length, paddedPlainText.length);
            System.out.println("Zero padding to " + (keyLength / 8));
            paddedPlainText = tmp;
        }
        PSource pSrc = (new PSource.PSpecified(iv));
        OAEPParameterSpec paramSpec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
        RSAPadding padding = RSAPadding.getInstance(RSAPadding.PAD_OAEP_MGF1, keyLength / 8, new SecureRandom(), paramSpec);
        System.out.println("PaddedPlainText length: " + paddedPlainText.length); //256
        byte[] plainText2 = padding.unpad(paddedPlainText, 0, paddedPlainText.length);
        System.out.println("Unpadded plain text: " + DatatypeConverter.printHexBinary(plainText2));
        return plainText2;
    } catch (GeneralSecurityException e) {
        e.printStackTrace();
        throw new Exception("Failed to decrypt AES secret key using RSA.", e);
    }
}
