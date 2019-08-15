public static void encryptToBinaryFile(String password, byte[] bytes, File file) throws EncrypterException {
    try {
        final byte[] rawKey = getRawKey(password.getBytes());
        final FileOutputStream ostream = new FileOutputStream(file, false);

        ostream.write(encrypt(rawKey, bytes));
        ostream.flush();
        ostream.close();

    } catch (IOException e) {
        throw new EncrypterException(e);
    }
}

private static byte[] encrypt(byte[] raw, byte[] clear) throws EncrypterException {
    try {
       final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
       final Cipher cipher = Cipher.getInstance("AES");
       cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

       return cipher.doFinal(clear);

    } catch (Exception e) {
        throw new EncrypterException(e);
    }
}
