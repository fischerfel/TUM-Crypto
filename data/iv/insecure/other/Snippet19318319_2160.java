public static String Decrypt(String text, String key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] keyBytes = new byte[16];
    byte[] b = key.getBytes("UTF-8");
    int len = b.length;
    if (len > keyBytes.length) {
        len = keyBytes.length;
    }
    System.arraycopy(b, 0, keyBytes, 0, len);

    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    BASE64Decoder decoder = new BASE64Decoder();
    byte[] results = cipher.doFinal(decoder.decodeBuffer(text));
    return new String(results, "UTF-8");
}
