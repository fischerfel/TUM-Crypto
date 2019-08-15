private static String getPassphraseSize16(String key) {
    if (TextUtils.isEmpty(key)) {
        return null;
    }
    char controlChar = '\u0014';
    String key16 = key + controlChar;
    if (key16.length() < 16) {
        while (key16.length() < 16) {
            key16 += key + controlChar;
        }
    }
    if (key16.length() > 16) {
        key16 = key16.substring(key16.length() - 16, key16.length());
    }
    return key16;
}


public static byte[] encodeAES(byte[] message, String passphrase) {
    String passphrase16 = getPassphraseSize16(passphrase);
    SecretKeySpec secretKey = new SecretKeySpec(passphrase16.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encodedText = cipher.doFinal(message);

    return encodedText;
}


public static byte[] decodeAES(byte[] encodedMessage, String key) {
    String passphrase16 = getPassphraseSize16(key);
    SecretKeySpec secretKey = new SecretKeySpec(passphrase16.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decodedText = cipher.doFinal(encodedMessage);

    return decodedText;
}
