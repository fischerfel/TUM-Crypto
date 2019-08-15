public static byte[] encryptWithAES(byte[] message, Key AESkey, byte[] iv) throws Exception {
byte[] decryptedMessage = null;
try {
    Cipher encrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");  // here is the change
    encrypt.init(Cipher.ENCRYPT_MODE, AESkey,new IvParameterSpec(iv)); // and I add the iv for cbc
    decryptedMessage = encrypt.doFinal(message);
} catch (Exception ex) {
    System.err.println(ex.getMessage());
    throw ex;
}
return decryptedMessage;
}
