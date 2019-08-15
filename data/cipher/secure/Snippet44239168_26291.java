public static String decrypt(byte[] text, PrivateKey key) {
    byte[] decryptedText = null;
    try {
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptedText = cipher.doFinal(text);
    } catch (Exception e) {
        e.printStackTrace();
    }

    return new String(decryptedText);
}
