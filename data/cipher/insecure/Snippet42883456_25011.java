public static byte[] encryptWithAES(byte[] message, Key AESkey) throws Exception {
    byte[] decryptedMessage = null;
    try {
        Cipher encrypt = Cipher.getInstance("AES");
        encrypt.init(Cipher.ENCRYPT_MODE, AESkey);
        decryptedMessage = encrypt.doFinal(message);
    } catch (Exception ex) {
        System.err.println(ex.getMessage());
        throw ex;
    }
    return decryptedMessage;
}
