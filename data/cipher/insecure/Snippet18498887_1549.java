private static final String DECRYPTION_ALGORITHM = "blowfish/ecb/nopadding";
private static final String KEY_ALGORITHM = "blowfish";
private static byte[] decrypt(byte[] keyData, byte[] valueData) throws Exception {
    SecretKeySpec keySpec = new SecretKeySpec(keyData, KEY_ALGORITHM);
    Cipher cipher = Cipher.getInstance(DECRYPTION_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    return cipher.doFinal(valueData);
}
