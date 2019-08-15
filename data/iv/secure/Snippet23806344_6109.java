public static byte[] encrypt(String input, String key, String iv) {

    byte[] raw = key.getBytes(Charset.forName("UTF8"));
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    try {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv.getBytes()));
        return cipher.doFinal(padString(input).getBytes("UTF-8"));
    } catch (Exception e) {
    }

    return new byte[0];
}
