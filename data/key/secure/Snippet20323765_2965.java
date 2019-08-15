public static String encrypt(String data) throws Exception {
    byte[] keyValue = encryptionKey.getBytes();
    Key key = new SecretKeySpec(keyValue, "AES");
    Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(data.getBytes());
    String encryptedValue = new BASE64Encoder().encode(encVal);
    return encryptedValue;
}
