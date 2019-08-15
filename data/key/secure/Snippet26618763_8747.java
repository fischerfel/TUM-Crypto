private static byte[] decrypt(byte[] rawKey, byte[] encrypted) throws Exception {
    SecretKeySpec spec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.DECRYPT_MODE, spec);

    return cipher.doFinal(encrypted); //THIS LINE THROWS EXCEPTION
}
