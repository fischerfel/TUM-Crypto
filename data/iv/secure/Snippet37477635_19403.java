public static String Decrypt(String text, String key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] keyBytes = new byte[16];
    byte[] b = key.getBytes("UTF-8");
    int len = b.length;
    if (len > keyBytes.length)
        len = keyBytes.length;
    System.arraycopy(b, 0, keyBytes, 0, len);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    byte[] results = new byte[text.length()];
    BASE64Decoder decoder = new BASE64Decoder();
    try {
        results = cipher.doFinal(decoder.decodeBuffer(text));
    } catch (Exception e) {
        System.out.print("Erron in Decryption");
    }
    return new String(results, "UTF-8");
}

public static String Encrypt(String text, String key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] keyBytes = new byte[16];
    byte[] b = key.getBytes("UTF-8");
    int len = b.length;
    if (len > keyBytes.length)
        len = keyBytes.length;
    System.arraycopy(b, 0, keyBytes, 0, len);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    System.out.println(keyBytes);
    System.out.println(keySpec);
    System.out.println(ivSpec);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

    byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(results);
}
