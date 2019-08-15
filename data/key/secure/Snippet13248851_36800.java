    public static byte[] encrypt(byte[] plainText, byte[] keyBytes)
        throws GeneralSecurityException {

    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] cipherText = cipher.doFinal(plainText);

    return cipherText;
}
