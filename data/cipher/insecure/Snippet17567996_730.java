public static String symmetricEncrypt(String text, String secretKey) {
    BASE64Decoder decoder = new BASE64Decoder();
    byte[] raw;
    String encryptedString;
    SecretKeySpec skeySpec;
    BASE64Encoder bASE64Encoder = new BASE64Encoder();
    byte[] encryptText = text.getBytes();
    Cipher cipher;
    try {
        raw = decoder.decodeBuffer(secretKey);
        skeySpec = new SecretKeySpec(raw, "AES");
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        encryptedString = bASE64Encoder.encode(cipher.doFinal(encryptText));
    } 
    catch (Exception e) {
        e.printStackTrace();
        return "Error";
    }
    return encryptedString;
}
