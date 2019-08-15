public byte[] Decrypt(byte[] encrypted) throws Exception {
    byte[] decodedValue = encrypted;
    Cipher c = getCipher(Cipher.DECRYPT_MODE);
    byte[] decValue = c.doFinal(decodedValue);
    return decValue;
}

private Cipher getCipher(int mode) throws Exception {
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] iv = getBytes(IV);
    c.init(mode, generateKey(), new IvParameterSpec(iv));
    return c;
}
