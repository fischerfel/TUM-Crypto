    private static final String Key = "0123456789abcdef";

public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
    return new SecretKeySpec(Key.getBytes("UTF-8"), "AES");
}

public static byte[] encryptMsg(String message, SecretKey secret)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

    Cipher cipher;
    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    return cipher.doFinal(message.getBytes("UTF-8"));
}

public static String decryptMsg(byte[] cipherText, SecretKey secret)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

    Cipher cipher;
    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret);
    return new String(cipher.doFinal(cipherText), "UTF-8");
}
