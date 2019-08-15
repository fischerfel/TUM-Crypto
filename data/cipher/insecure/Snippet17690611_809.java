public static String decrypt(byte[] message) throws Exception {
    final MessageDigest md = MessageDigest.getInstance("SHA-1");
    final byte[] digestOfPassword = md.digest(token.getBytes("utf-8"));
    final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
    for (int j = 0, k = 16; j < 8;) {
        keyBytes[k++] = keyBytes[j++];
    }

    final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    // final Cipher decipher = Cipher.getInstance("DESede/CFB/NoPadding");
    decipher.init(Cipher.DECRYPT_MODE, key, iv);
    final byte[] plainText = decipher.doFinal(message);
    return new String(plainText, "UTF-8");
}
