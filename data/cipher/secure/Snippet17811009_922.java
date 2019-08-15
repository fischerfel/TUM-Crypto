public String RSAEncrypt(String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException {

    publicKey = getPublicKey();
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] cipherData = cipher.doFinal(plain.getBytes());
    return Base64.encodeToString(cipherData, Base64.DEFAULT);
}

public String RSADecrypt(byte[] encryptedBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException {

    privateKey = getPrivateKey();
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);      
    byte[] cipherData = cipher.doFinal(encryptedBytes);
    return Base64.encodeToString(cipherData, Base64.DEFAULT);
}
