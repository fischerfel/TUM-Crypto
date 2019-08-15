public static String encryptWithAES(String payload, String aesKey) {

    byte[] raw = aesKey.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES/ECB/PKCS5Padding");
    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted;
        encrypted = cipher.doFinal(payload.getBytes());
        cipher = null;
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    } catch (Exception e) {
        System.out.println("Error in encryptWithAES!!!");
        e.printStackTrace();
    }
    return null;
}
