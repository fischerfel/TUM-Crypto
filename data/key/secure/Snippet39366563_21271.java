public static String encryptMethod(String seedBase64, byte[] key) {
    try {
        byte[] seed = Base64.decode(seedBase64, 0);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(1, keySpec);
        return Base64.encodeToString(cipher.doFinal(seed), 0);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
