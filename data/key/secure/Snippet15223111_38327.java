public static String AESencrypt(String valueToEnc) throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encValue = c.doFinal(valueToEnc.getBytes());
    String encryptedValue = new Base64().encodeBase64(encValue).toString();
    return encryptedValue;
}

public static String AESdecrypt(String encryptedValue) throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = new Base64().decodeBase64(encryptedValue);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
}
