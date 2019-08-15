public static String encrypt(String message, String key) throws Exception {
    final MessageDigest md = MessageDigest.getInstance("md5");
    final byte[] digestOfPassword = md.digest(key.getBytes("utf-8"));
    final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
    for (int j = 0, k = 16; j < 8;) {
        keyBytes[k++] = keyBytes[j++];
    }

    final SecretKey keyz = new SecretKeySpec(keyBytes, "DESede");
    final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, keyz, iv);

    final byte[] plainTextBytes = message.getBytes("utf-8");
    final byte[] cipherText = cipher.doFinal(plainTextBytes);
    final String encodedCipherText = new   sun.misc.BASE64Encoder().encode(cipherText);

    return encodedCipherText;
}
