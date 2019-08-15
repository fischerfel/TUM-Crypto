public static String AESencrypt(String valueToEnc) throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encValue = c.doFinal(valueToEnc.getBytes());
    return encValue.toString();
}

public static String AESdecrypt(String encryptedValue) throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decValue = c.doFinal(encryptedValue.getBytes());
    String decryptedValue = new String(decValue);
    return decryptedValue;
}
