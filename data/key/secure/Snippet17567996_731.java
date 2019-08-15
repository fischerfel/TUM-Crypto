public static String symmetricDecrypt(String text, String secretKey) {
    BASE64Decoder decoder = new BASE64Decoder();
    BASE64Decoder base64Decoder = new BASE64Decoder();
    Cipher cipher;
    String encryptedString;
    byte[] encryptText = null;
    byte[] raw;
    SecretKeySpec skeySpec;
    try {
        raw = decoder.decodeBuffer(secretKey);
        skeySpec = new SecretKeySpec(raw, "AES");
        encryptText = base64Decoder.decodeBuffer(text);
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        encryptedString = new String(cipher.doFinal(encryptText));
    } catch (Exception e) {
        e.printStackTrace();
        return "Error";
    }
    return encryptedString;
}
