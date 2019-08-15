String Encrypt(String text, byte[] keyBytes) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec("AAAAAAAAAAAAAAAA".getBytes("UTF-8"));
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

    byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
    return Base64.encodeToString(results, Base64.URL_SAFE);
}
