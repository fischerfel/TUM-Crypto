private static byte[] key = "12345678".getBytes();// 64 bit 
private static byte[] iv = "12345678".getBytes();

public static String encrypt(String in) {
    String cypert = in;
    try {
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec k = new SecretKeySpec(key, "DES");
        Cipher c = Cipher.getInstance("DES/CBC/PKCS7Padding");
        c.init(Cipher.ENCRYPT_MODE, k, ivSpec);
        byte[] encryptedData = c.doFinal(in.getBytes());
        cypert = Base64.encodeLines(encryptedData);
    } catch (Exception e) {
        Debugger.error(e);
    }
    return cypert;
}


public static String decrypt(String in) throws Exception {
    String plain=in;
    try {
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec keys = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, keys, ivSpec);
        // decryption pass
        byte[] cipherText = Base64.decodeLines(in);
        int ctLength = cipherText.length;
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(cipher.doFinal(cipherText));
        plainText = bos.toByteArray();
        bos.close();
        plain = new String(plainText, "UTF8");
    } catch (Exception e) {
        Debugger.error(e);
    }
    return plain;
}
