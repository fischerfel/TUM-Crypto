private void createAndSendAES() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, InvalidKeyException, BadPaddingException {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    this.AESBlackboardKey = keyGen.generateKey(); // My AES key

     byte[] raw = AESBlackboardKey.getEncoded();
     System.out.println(raw.length); // Prints 16

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, this.clientPubKey);


    SealedObject encryptedAESBlackboardKey = new SealedObject(this.AESBlackboardKey, cipher); // ERROR HERE

    ObjectOutputStream outO = new ObjectOutputStream(this.clientSocket.getOutputStream());
    outO.writeObject(encryptedAESBlackboardKey); //Transmitting the key over socket link
    outO.flush();

    System.out.println("AS: Blackboard AES key sent.");

}
