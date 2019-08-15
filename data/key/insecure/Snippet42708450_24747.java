    private static byte[] seedValue = {
        0x2d, 0x2a, 0x2d, 0x42, 0x55, 0x49, 0x4c, 0x44, 0x41, 0x43, 0x4f, 0x44, 0x45, 0x2d, 0x2a, 0x2d
};
private static String ALGORITHM = "AES";
private static SecretKeySpec secretKey = new SecretKeySpec(seedValue, "AES");


public static String encrypt( String data ) throws Exception {
    try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(data.getBytes("UTF8"));
        String encryptedString = new String(Base64.encode(cipherText ,Base64.DEFAULT ) );
        return encryptedString;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public static String decrypt(String data) throws Exception {
    try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] cipherText = Base64.decode(data.getBytes("UTF8"), Base64.DEFAULT);
        String decryptedString = new String(cipher.doFinal(cipherText),"UTF-8");
        return decryptedString;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
