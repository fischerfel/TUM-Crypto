public static byte[] encryptRSA(String str, PublicKey pubKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    return cipher.doFinal(str.getBytes());
}

String cipher = Base64.getEncoder().encodeToString(Encryption.encryptRSA("0123456789ABCDEF", pubKeyk));
