 public static String harden(String unencryptedString) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String key ="***************";
        MessageDigest md = MessageDigest.getInstance("md5");
    byte[] digestOfPassword = md.digest(key.getBytes("utf-8"));
    byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

    for (int j = 0, k = 16; j < 8;) {
        keyBytes[k++] = keyBytes[j++];
    }

    SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    byte[] plainTextBytes = unencryptedString.getBytes("utf-8");
    byte[] buf = cipher.doFinal(plainTextBytes);
    byte[] base64Bytes;

           base64Bytes = Base64.getEncoder().encode(buf);

    String base64EncryptedString = new String(base64Bytes);

    return base64EncryptedString;
}
