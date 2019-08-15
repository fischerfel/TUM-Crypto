private byte[] encrypt(String key, String plainText) throws GeneralSecurityException {

    SecretKey secret_key = new SecretKeySpec(key.getBytes(), ALGORITM);

    Cipher cipher = Cipher.getInstance(ALGORITM);
    cipher.init(Cipher.ENCRYPT_MODE, secret_key);

    return cipher.doFinal(plainText.getBytes());
}
