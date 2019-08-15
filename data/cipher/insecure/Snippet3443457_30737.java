public static byte[] encryptTripleDES(String message) throws Exception {
    final MessageDigest md = MessageDigest.getInstance("md5");
    final byte[] digestOfPassword = md.digest("--KEY--".getBytes("utf-8"));
    final SecretKey key = new SecretKeySpec(digestOfPassword, "DESede");
    final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    return cipher.doFinal(message.getBytes("utf-8"));
}
