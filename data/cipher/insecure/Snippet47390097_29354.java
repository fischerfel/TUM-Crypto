    public static String desEncrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
{
    String keyString = AESEncryption.convertToUTF8("8DJE7K01U8B51807B3E17D21");

    byte[] keyValue = Base64.getEncoder().encode(keyString.getBytes(StandardCharsets.UTF_8));
    Key key = new SecretKeySpec(keyValue, "AES");
    Cipher c1 = Cipher.getInstance("AES/ECB/PKCS5Padding");

    c1.init(Cipher.DECRYPT_MODE, key);
    byte[]   encodedText = Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8));
    byte[] encVal = c1.doFinal(encodedText);

    System.out.println(new String(encodedText));
    return new String(encVal,StandardCharsets.UTF_8);

}
