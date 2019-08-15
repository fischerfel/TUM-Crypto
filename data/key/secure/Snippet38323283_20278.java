public static String encrypt(String input, String key) {
    try {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return new String(Base64.encode(cipher.doFinal(input.getBytes()),Base64.DEFAULT));
    } catch (Exception e) {

    }
    return null;
}
