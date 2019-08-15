    public byte[] encryptMessage(String plainText, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    System.out.print("\n Plaintext : " + plainText + "\n");
    byte[] t0 = plainText.getBytes();
    for (byte b : t0) System.out.printf("%02X ", b);
    System.out.println("\n Plaintext Length : " + t0.length + " byte");

    System.out.println("\n=== RSA Encryption ===");
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] b0 = cipher.doFinal(t0);
    System.out.print("\n\n Ciphertext : ");
    for (byte b : b0) System.out.printf("%02X ", b);
    System.out.println("\n Ciphertext Length : " + b0.length + " byte");

    return b0;
}
