public static String encrypt(String plainText, Key publicKey) throws Exception {
    Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));
    return Base64.encodeToString(cipherText, Base64.NO_WRAP);
}

public static String decrypt(String cipherText, Key privateKey) throws Exception {
    byte[] bytes = Base64.decode(cipherText, Base64.NO_WRAP);

    Cipher decriptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

    return new String(decriptCipher.doFinal(bytes), "UTF-8");
}
