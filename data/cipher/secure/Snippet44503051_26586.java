public static String decrypt(String src) {
    String decrypted = "";
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
        byte[] array = Base64.decode(src);
        byte[] encrypted = Arrays.copyOfRange(array, 16, array.length);
        decrypted = new String(cipher.doFinal(encrypted));
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    return decrypted;
}
