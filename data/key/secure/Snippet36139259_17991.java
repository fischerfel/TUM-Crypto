String initializer = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

byte[] initializer2 = new BigInteger(initializer, 2).toByteArray();

String encrypt = encrypt(binary_string_firsthalf, initializer2);

public static String encrypt(String plainText, byte[] key) {
    try {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
        String encryptedString = new String(Base64.encode(cipherText));
        return encryptedString;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
