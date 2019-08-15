public String notify(String message, String encryptionKey) {
    Security.addProvider(new BouncyCastleProvider());
    // System.out.println(message);
    byte[] key = Base64.decode(encryptionKey);
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    byte[] data = Base64.decode(message);
    String decryptedString = "";
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(data);
        decryptedString = new String(decrypted);
    } catch (Exception e) {
        e.printStackTrace();
    }
    System.out.println(decryptedString);
    return decryptedString;
}
