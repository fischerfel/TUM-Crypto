private static String encrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException
{
    String keyString = AESEncryption.convertToUTF8("8DJE7K01U8B51807B3E17D21");
    text = AESEncryption.convertToUTF8(text);

    byte[]keyValue = Base64.getEncoder().encode(keyString.getBytes(StandardCharsets.UTF_8));
    Key key = new SecretKeySpec(keyValue, "AES");
    Cipher c1 = Cipher.getInstance("AES/ECB/PKCS5Padding");

    c1.init(Cipher.ENCRYPT_MODE, key);

    byte[] encodedText =Base64.getEncoder().encode(text.getBytes(StandardCharsets.UTF_8));
    System.out.println("Encoded text: "+new String(encodedText,StandardCharsets.UTF_8));

    byte[] encVal = c1.doFinal(encodedText);
    System.out.println("Encoded val: "+new String(encVal,StandardCharsets.UTF_8));

    return new String(encVal);
}
