Cipher c = null;
try {
    c = Cipher.getInstance("RSA");
} catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
    Logger.getLogger(SecureServer.class.getName()).log(Level.SEVERE, null, ex);
}
 {
    try {
        c.init(Cipher.ENCRYPT_MODE, publicKeyofClient);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(SecureServer.class.getName()).log(Level.SEVERE, null, ex);
    }
}
byte[] encryptedBytes = null;
 {
    try {
        encryptedBytes = c.doFinal(ServerSecretKey.getEncoded());
    } catch (IllegalBlockSizeException | BadPaddingException ex) {
        Logger.getLogger(SecureServer.class.getName()).log(Level.SEVERE, null, ex);
    }
}

OutputStream outputStream;
outputStream = clientSocket.getOutputStream();
outputStream.write(encryptedBytes, 0, encryptedBytes.length);

outputStream.flush();
outputStream.close();
clientSocket.close(); // closing the connection
