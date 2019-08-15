public static String encrypt(String input, String key){
    byte[] crypted = null;

    SecretKeySpec skey = new SecretKeySpec(getHash("MD5", key), "AES");
    IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skey, iv);
    crypted = cipher.doFinal(input.getBytes());
    return new String(Base64.encodeBase64(crypted));
}

 private static byte[] getHash(String algorithm, String text) {
    try {
        byte[] bytes = text.getBytes("UTF-8");
        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(bytes);
        return digest.digest();
    } catch (final Exception ex) {
        throw new RuntimeException(ex.getMessage());
    }
}
