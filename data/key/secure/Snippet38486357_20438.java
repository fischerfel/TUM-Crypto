 public static String aes_decrypt(String passwordhex, String strKey) throws Exception {
    try {
        byte[] keyBytes = Arrays.copyOf(strKey.getBytes("ASCII"), 16);

        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        Cipher decipher = Cipher.getInstance("AES");

        decipher.init(Cipher.DECRYPT_MODE, key);

        char[] cleartext = passwordhex.toCharArray();

        byte[] decodeHex = Hex.decodeHex(cleartext);

        byte[] ciphertextBytes = decipher.doFinal(decodeHex);

        return new String(ciphertextBytes);

    } catch (Exception e) {
        e.getMessage();
    }
    return null;
}
