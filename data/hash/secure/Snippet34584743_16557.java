public static String encrypt(String src) {
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        Key key = makeKey();
        AlgorithmParameterSpec iv = makeIv();

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return Base64.encodeBytes(cipher.doFinal(src.getBytes()));
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

 static AlgorithmParameterSpec makeIv() {
        return new IvParameterSpec(ENCRYPTION_IV.getBytes(encoding));
}


static Key makeKey() {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] key = md.digest(ENCRYPTION_KEY.getBytes(encoding));
        return new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return null;
}
