public static String decrypt(byte[] keyValue, String ivValue, String encryptedData) throws Exception {
    Key key = new SecretKeySpec(keyValue, "AES");
    byte[] iv = Base64.decode(ivValue.getBytes("UTF-8"), Base64.DEFAULT);
    byte[] decodedValue = Base64.decode(encryptedData.getBytes("UTF-8"), Base64.DEFAULT);

    Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding"); // or PKCS5Padding
    c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    byte[] decValue = c.doFinal(decodedValue);

    int firstQuoteIndex = 0;
    while(decValue[firstQuoteIndex] != (byte)'"') firstQuoteIndex++;
    return new String(Arrays.copyOfRange(decValue, firstQuoteIndex + 1, decValue.length-2));
}
