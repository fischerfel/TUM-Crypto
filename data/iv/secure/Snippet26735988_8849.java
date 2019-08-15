public static String decrypt(final String cipherText, final String passPhrase, final String saltValue, final int passwordIterations, final String initVector, final int keySize)
    throws Exception {
    final byte[] initVectorBytes = initVector.getBytes("ASCII");
    final byte[] saltValueBytes = saltValue.getBytes("ASCII");
    final byte[] cipherTextBytes = Base64.decode(cipherText);
    final PKCS5S1ParametersGenerator generator = new PasswordDeriveBytes(new SHA1Digest());
    generator.init(passPhrase.getBytes("ASCII"), saltValueBytes, passwordIterations);
    final byte[] key = ((KeyParameter) generator.generateDerivedParameters(keySize)).getKey();
    final SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
    final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    final IvParameterSpec iv = new IvParameterSpec(initVectorBytes);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
    final byte[] decryptedVal = cipher.doFinal(cipherTextBytes);
    return new String(decryptedVal);
}
