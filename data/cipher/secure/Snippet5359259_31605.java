public static String getEncryptedMessage(String publicKeyFilePath,

    String plainMessage) {
    byte[] encryptedBytes;
    try {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] publicKeyContentsAsByteArray = getBytesFromFile(publicKeyFilePath);
        PublicKey publicKey = getPublicKey(publicKeyContentsAsByteArray);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(plainMessage.getBytes());
        return new String(encryptedBytes);
    } catch (Throwable t) {

    }

}
public static String getDecryptedMessage(
        String privateKeyFilePath, String encryptedMessage)
         {
    byte[] decryptedMessage;
    try {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] privateKeyContentsAsByteArray = getBytesFromFile(privateKeyFilePath);
        PrivateKey privateKey = getPrivateKey(privateKeyContentsAsByteArray);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedMessage = cipher.doFinal(encryptedMessage.getBytes());
        return new String(decryptedMessage);
    } catch (Throwable t) {


}
