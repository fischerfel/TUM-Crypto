public String decrypt(String message, RSAPrivateKey key) {
    try {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] dataEncripted =Base64.decodeBase64(message.getBytes());
        byte[] data = cipher.doFinal(dataEncripted);
        return new String(data,"UTF-8");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
