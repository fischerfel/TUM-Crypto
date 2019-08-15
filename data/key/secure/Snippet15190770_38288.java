public static String encrypt(byte[] raw, byte[] clear) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    byte[] encrypted = cipher.doFinal(clear);
    return android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP);
}

public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);

    byte[] decrypted = cipher.doFinal(encrypted);
    return decrypted;
}
