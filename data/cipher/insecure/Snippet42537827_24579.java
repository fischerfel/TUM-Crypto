private OutputStream getEncryptedStream(OutputStream ouputStream) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, this.key);
    CipherOutputStream output = new CipherOutputStream(ouputStream, cipher);

    return output;
}
