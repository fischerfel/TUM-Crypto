public static String encrypt(String source, String publicKey)
            throws Exception {
    Key key = getPublicKey(publicKey);
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] b = source.getBytes();
    byte[] b1 = cipher.doFinal(b);
    return new String(Base64.encodeBase64(b1), "UTF-8");
}
