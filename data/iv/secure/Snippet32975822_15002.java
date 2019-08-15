public static String decrypt(byte[] iv, String encryptedData) throws Exception {
    byte[] keyValue = "zy2dEd1pKG5i3WuWbvOBolFQR84AYbvN".getBytes();
    Key key = new SecretKeySpec(keyValue, "AES");
    Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
    c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    byte[] decordedValue = Base64.decode(encryptedData.getBytes(), Base64.DEFAULT);
    byte[] decValue = c.doFinal(decordedValue);
    return new String(decValue);
}
