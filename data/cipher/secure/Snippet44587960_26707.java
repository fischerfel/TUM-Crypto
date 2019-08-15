public static String encrypt(String src) {
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
        return Base64.encodeBytes(cipher.doFinal(src.getBytes()));
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
