private void exampleMethod(){
    String messageString = "Why does this not work in Android?";

    String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoApIBIna77xq4j+M2RmyIhsB++41NHcY4KIPfX4VP4ADnkO+7ejbs4le/twrPtYGESVPF9czSMB5bzmTBZtq0jC8oT/6wiDIBlSuzo4fBrGociBIuaOjyG/j3ZhpcWpWPXuzER+ehuQ+8hZkMuJdK9IodqPR+5jmCef4rXoKObwS02LYQ1co5dEmtZVQRmmeYaVnWibd/s1d4KKGvSzXap3YBTf8peH5UGIQrLOTqvX0bo34xFxmj5U0H3xudnnwuVAlQlj9KiHPPABuwNtm1buRKJb5HZhSCveyT/2YAOmQqGrVN/nALtlZyTDZNs//Vp1zb9exSuG0t5xFc+pn4QIDAQAB";

    String encryptedMessageString = getUrlEncodedCipherText(messageString, serverPubKey, Base64.NO_WRAP);
    /**
     * CipherText is ALWAYS the same and does not decrypt: DA_-RpCki-mjF6tSwiP2IhuW2UfPzZC7A9oVTTNptjT73HtROiQZvUC0Z2veJ5VwVx4toolvLErQmKKoQlqELSD756bu8ohEQwgJ4Xsu-3tXv-uEi5a9a_u19WnNLIF7tayDUhFeD2RzNvW895y1v-D30TvQRskNCFJfnjaytr_vmcVv8HrXURCmG6AMltaqdN72zh8p6VkKcjXSLiCApH957GXSqJCRzxbaQwf8X5EJfn8CQrPDGbE3gdhc2_hFwXQNIdxPxrOLtVbaFp9i_4GRWXJ6E2jHttV2bDv_uSVIz3OBzh7EkJiCnl3c904sH8QZae8c3SQyrTxVL7EpIA,,
     */
}

public static String getUrlEncodedCipherText(String plainText, String pubKey, int base64Type){
    try {
        final PublicKey publicKey = loadPublicKey(pubKey, base64Type);
        final byte[] cipherBytes = encrypt(plainText, publicKey);
        String cipherText = base64Encode(cipherBytes, base64Type);
        String urlEncodedCipherText = urlEncode(cipherText);
        return urlEncodedCipherText;
    }
    catch (Exception e){
        e.printStackTrace();
        return null;
    }
}

public static final String ALGORITHM = "RSA";

public static PublicKey loadPublicKey(String stored, int base64Type) throws GeneralSecurityException {
    String pubKey = stored.replace(BEGIN_PUBLIC_KEY, "");
    pubKey = pubKey.replace(END_PUBLIC_KEY, "");

    byte[] data = Base64.decode(pubKey, base64Type);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
    PublicKey pub = fact.generatePublic(spec);
    return pub;
}

public static byte[] encrypt(String text, PublicKey key) {
    byte[] cipherText = null;
    try {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text.getBytes());
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return cipherText;
}

public static String base64Encode(byte[] cipherBytes, int base64Type){
    byte[] base64Cipher = Base64.encode(cipherBytes, base64Type);
    return new String(base64Cipher);
}

public static String urlEncode(String text){
    return text.replace("+", "-").replace("/", "_").replace("=", ",");
}
