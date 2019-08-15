private byte[] encryptAES(SecretKeySpec key, byte[] text) {
    byte[] encryptedText = null;
    try {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        encryptedText = cipher.doFinal(text);
    } catch (GeneralSecurityException e) {
        System.out.println("[ERROR] Could not encrypt data!");
        e.printStackTrace();
    }
    return encryptedText;
}
