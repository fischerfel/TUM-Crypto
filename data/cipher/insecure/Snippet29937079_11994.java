public static byte[] encryptAES(SecretKey key, byte[] clear) {
    try {

        SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
