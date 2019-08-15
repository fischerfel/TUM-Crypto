private void testEncryption(byte[] plainText, PrivateKey privateKey, PublicKey publicKey, int keyLength) throws GeneralSecurityException {

    System.out.println("Plain text: " + DatatypeConverter.printHexBinary(plainText));

    Provider bcProvider = new BouncyCastleProvider();
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING", bcProvider);
    rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] cipherText = rsaCipher.doFinal(plainText);

    System.out.println("Cipher text: " + DatatypeConverter.printHexBinary(cipherText));

    Provider pkcs11provider = new SunPKCS11("C:\\Users\\manishs525\\pkcs11.cfg");
    Cipher rsaCipher2 = Cipher.getInstance("RSA/ECB/NoPadding", pkcs11provider);
    rsaCipher2.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] paddedPlainText = rsaCipher2.doFinal(cipherText);

    /* Ensure leading zeros not stripped */
    if (paddedPlainText.length < keyLength / 8) {
        byte[] tmp = new byte[keyLength / 8];
        System.arraycopy(paddedPlainText, 0, tmp, tmp.length - paddedPlainText.length, paddedPlainText.length);
        System.out.println("Zero padding to " + (keyLength / 8));
        paddedPlainText = tmp;
    }           

    System.out.println("OAEP padded plain text: " + DatatypeConverter.printHexBinary(paddedPlainText));

    OAEPParameterSpec paramSpec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1,
            PSource.PSpecified.DEFAULT);
    RSAPadding padding = RSAPadding.getInstance(RSAPadding.PAD_OAEP_MGF1, keyLength / 8, new SecureRandom(), paramSpec);
    byte[] plainText2 = padding.unpad(paddedPlainText);

    System.out.println("Unpadded plain text: " + DatatypeConverter.printHexBinary(plainText2));
}
