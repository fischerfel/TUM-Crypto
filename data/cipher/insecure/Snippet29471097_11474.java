 private static final String ALGORITHM = "AES";
 private static final byte[] keyValue =
        new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };


 public String encrypt(String valueToEnc) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encValue = c.doFinal(valueToEnc.getBytes());
    // String encryptedValue = new Base64.encoder();

    return Base64.encodeToString(encValue, Base64.DEFAULT);
}

public String decrypt(String encryptedValue) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.DECRYPT_MODE, key);
    //byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
    byte[] decordedValue = Base64.decode(encryptedValue, Base64.DEFAULT);

    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
}
