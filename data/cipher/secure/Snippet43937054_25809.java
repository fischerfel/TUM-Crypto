private static byte[] encrypt(byte[] key, byte[] text) throws GeneralSecurityException {
    final SecretKeySpec skeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
    final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, sInitVectorSpec);
    return cipher.doFinal(text);
}
