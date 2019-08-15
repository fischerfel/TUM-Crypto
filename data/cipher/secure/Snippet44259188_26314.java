public static void main(String[] args) {
    try {
        // 128 bits key
        openssl_encrypt("hello", "bbbbbbbbbbbbbbbb", "aaaaaaaaaaaaaaaa");
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}

private static String openssl_encrypt(String data, String strKey, String strIv) throws Exception {
    Base64 base64 = new Base64();
    Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec key = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
    IvParameterSpec iv = new IvParameterSpec(strIv.getBytes("UTF-8"), 0, ciper.getBlockSize());

    // Encrypt
    ciper.init(Cipher.ENCRYPT_MODE, key, iv);
    byte[] encryptedCiperBytes = base64.encode((ciper.doFinal(data.getBytes())));

    String s = new String(encryptedCiperBytes);
    System.out.println("Ciper : " + s);
    return s;
}
