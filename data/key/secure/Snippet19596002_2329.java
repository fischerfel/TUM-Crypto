private static byte[] translate(byte[] val, int mode) throws Exception {
    if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE)
        throw new IllegalArgumentException(
                "Encryption invalid. Mode should be either Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE");
    SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    IvParameterSpec ivSpec = new IvParameterSpec(generateIv());
    cipher.init(mode, skeySpec, ivSpec);
    byte[] encrypted = cipher.doFinal(val);
    return encrypted;
}
