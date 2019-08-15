String Decrypt(String text, String key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] keyBytes = Base64.decode(key.getBytes(), Base64.DEFAULT);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    byte[] b = new byte[keySpec.getEncoded().length];
    System.arraycopy(keySpec.getEncoded(), 0, b, 0, b.length);
    IvParameterSpec ivSpec = new IvParameterSpec(b);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    byte[] results = cipher.doFinal(Base64.encode(text.getBytes("UTF-8"), Base64.DEFAULT));
    String  decoded = new String(cipher.doFinal(results), "UTF-8");
    return decoded;
}
