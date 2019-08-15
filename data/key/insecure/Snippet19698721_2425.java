protected static String encrypt(String plaintext) {
    final byte[] KEY = {
            0x6d, 0x79, 0x56, 0x65, 0x72, 0x79, 0x54, 0x6f, 0x70,
            0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b
    };

    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        final SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        final String encryptedString = Base64.encodeToString(
            cipher.doFinal(plaintext.getBytes()), Base64.DEFAULT);

        return encryptedString;
    } catch (Exception e) {
        return null;
    }
}
