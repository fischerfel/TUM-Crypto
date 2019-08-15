public static String getEncryptedLogin(String loginID, String encryptionKey) {
    byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "RIJNDAEL");

    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
    byte[] result = cipher.doFinal(loginID.getBytes("UTF-8"));
    return Base64.getEncoder().encodeToString(result);
}
