public static String decryptWithIV(byte[] key, String encrypted)
        throws GeneralSecurityException {

    if (key.length != 16) {
        throw new IllegalArgumentException("Invalid key size.");
    }
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
            new byte[16]));
    byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted
            .getBytes()));

    return new String(original);
}
