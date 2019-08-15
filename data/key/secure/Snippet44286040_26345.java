private static String openssl_encrypt(String data, String strKey, String strIv) throws Exception {
    Base64 base64 = new Base64();
    Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec key = new SecretKeySpec(strKey.getBytes(), "AES");
    IvParameterSpec iv = new IvParameterSpec(strIv.getBytes(), 0, ciper.getBlockSize());

    // Encrypt
    ciper.init(Cipher.ENCRYPT_MODE, key, iv);
    byte[] encryptedCiperBytes = ciper.doFinal(data.getBytes());

    String s = new String(encryptedCiperBytes);
    System.out.println("Ciper : " + s);
    return s;
}
