  private static byte[] decrypt(byte[] raw, byte[] encrypted) throws EncrypterException {
SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
try {
    final Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);

    return cipher.doFinal(encrypted);

} catch (Exception e) {
    throw new EncrypterException(e);
}
}
