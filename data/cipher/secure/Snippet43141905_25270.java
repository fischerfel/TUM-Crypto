public static byte[] encryptWithRSA(byte[] data,PublicKey clientKey) throws Exception {
    byte[] cipherText = null;
    try {
        System.out.println("Start encryptWithRSA ");
        Cipher encrypt = Cipher.getInstance("RSA/ECB/NoPadding", "BC");
        encrypt.init(Cipher.ENCRYPT_MODE, clientKey);
        cipherText = encrypt.doFinal(data); 
    } catch (Exception ex) {
        System.err.println(ex.getMessage);
        throw ex;
    }
    return cipherText;
}

public static byte[] decryptWithRSA(byte[] encryptedMessage,PrivateKey serverKey) throws Exception {
    byte[] decryptedMessage = null;
    try {
        System.out.println("Start decryptWithRSA ");
        Cipher decrypt = Cipher.getInstance("RSA/ECB/NoPadding", "BC");
        decrypt.init(Cipher.DECRYPT_MODE, serverKey);
        decryptedMessage = decrypt.doFinal(encryptedMessage);
    } catch (Exception ex) {
        System.err.println(ex.getMessage);
        throw ex;
    }
    return decryptedMessage;
}
