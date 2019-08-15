public static byte[] decryptAES(SecretKey key, byte[] encrypted) {
    try {

        SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
