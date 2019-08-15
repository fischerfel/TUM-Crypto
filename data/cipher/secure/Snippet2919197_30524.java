ObjectInputStream inServ3 = new ObjectInputStream(socket.getInputStream());
try{
    ParamClass print5 = (ParamClass) (inServ3.readObject());
    Cipher dec = Cipher.getInstance("RSA");
    dec.init(Cipher.DECRYPT_MODE, serverPrivateKey);
    byte[] dectyptedText = dec.doFinal(print5.cipherText);
    String decipherText = dectyptedText.toString();
    System.out.println("\nDecrypted Message to string: " + decipherText);
}catch (Throwable e) {
    e.printStackTrace();
    System.out.println("Unable to perform RSA decryption!");
}
