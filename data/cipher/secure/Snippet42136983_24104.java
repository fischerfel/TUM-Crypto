private static final String AES_KEY = "something";

String encodedText = null;
try {
    final MessageDigest md = MessageDigest.getInstance("SHA-256");
    final byte[] digestOfPassword = md.digest(AES_KEY.getBytes("utf-8"));
    final SecretKey key = new SecretKeySpec(digestOfPassword, "AES");
    final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    final byte[] plainTextBytes = orignalText.getBytes("utf-8");
    final byte[] encodeTextBytes = cipher.doFinal(plainTextBytes);

    encodedText = new Base64().encodeToString(encodeTextBytes);

}
