public static String aesEncrypt(String value, String key, String initVector) {
    try {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "Rijndael");
        Cipher cipher = Cipher.getInstance("Rijndael/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}
