public static SecretKeySpec generateKey(byte[] secretKey) {

    try {
        // 256 bit key length
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(secretKey);
        byte[] key = md.digest();
        return new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
