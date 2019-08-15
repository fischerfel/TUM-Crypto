private byte[] decryptAES(SecretKeySpec key, byte[] text) {
    byte[] decryptedText = null;
    try {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptedText = cipher.doFinal(text);
    } catch (GeneralSecurityException e) {
        System.out.println("[WARNING] Could not decrypt data, wrong key?");
    }
    return decryptedText;
}
