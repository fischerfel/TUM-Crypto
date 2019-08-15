static void setKey(byte[] keybytes, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    key = new SecretKeySpec(keybytes, "AES");
    ivspec = new IvParameterSpec(iv);
    encryptcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    encryptcipher.init(Cipher.ENCRYPT_MODE, key,ivspec);

    decryptcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    decryptcipher.init(Cipher.DECRYPT_MODE, key,ivspec);        
}
