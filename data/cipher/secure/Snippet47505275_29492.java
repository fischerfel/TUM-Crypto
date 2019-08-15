private void decryptData(byte[] data) throws IOException {
    System.out.println("\n----------------DECRYPTION STARTED------------");
    byte[] descryptedData = null;
    try {
        PrivateKey privateKey = readPrivateKeyFromFile(PRIVATE_KEY_FILE);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        descryptedData = cipher.doFinal(data);
        System.out.println("Decrypted Data: " + new String(descryptedData));
    } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
    }
    System.out.println("----------------DECRYPTION COMPLETED------------");
}
