public static String AesEncrypt(String encryptedMessage){
    try {
        IvParameterSpec initialVector = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initialVector);

        byte[] encrypted = cipher.doFinal(encryptedMessage.getBytes());
        String base64 = StringUtils.newStringUtf8(Base64.encodeBase64(encrypted, false));

        return toHex(base64);
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}
