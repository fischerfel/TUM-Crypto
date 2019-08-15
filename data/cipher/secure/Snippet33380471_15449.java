public String encrypt(String message, String privateKey) throws Exception{
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    IvParameterSpec iv = makeIv();
    cipher.init(Cipher.ENCRYPT_MODE, makeKey(privateKey), iv);
    return Base64.encodeToString(cipher.doFinal(message.getBytes()), Base64.DEFAULT);
}
