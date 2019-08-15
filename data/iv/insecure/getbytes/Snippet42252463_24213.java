public static String AesEncrypt(String encryptedMessage){
    try {
        IvParameterSpec initialVector = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initialVector);

        byte[] encrypted = cipher.doFinal(encryptedMessage.getBytes());

        byte[] base64 = Base64.encodeBase64(encrypted);

        StringBuilder encryptedSb = new StringBuilder(base64.length);
        for (byte i : base64){
            encryptedSb.append(i);
        }
        return toHex(encryptedSb.toString());
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}
