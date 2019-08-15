public static String encrypt(String key, String value) {
    try {
        IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));

        return Base64.encodeBase64String(encrypted);
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return null;
}
