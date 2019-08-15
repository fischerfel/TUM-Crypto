public String RSADecrypt(final byte[] encryptedBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    cipher1 = Cipher.getInstance("RSA");
    cipher1.init(Cipher.DECRYPT_MODE, privateKey);
    decryptedBytes = cipher1.doFinal(encryptedBytes);
    decrypted = new String(decryptedBytes);
    System.out.println("DDecrypted?????" + decrypted);
    return decrypted;
}
