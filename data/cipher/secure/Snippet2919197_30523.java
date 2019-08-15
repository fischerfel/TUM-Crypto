try{
    Cipher c = Cipher.getInstance("RSA");
    c.init(Cipher.ENCRYPT_MODE, servPubKey);
    String myMessage = "this is a secret message";
    byte[] msgByte = myMessage.getBytes();
    ObjectOutputStream outVehicle3 = new ObjectOutputStream(socket.getOutputStream());
    ParamClass print4 = new ParamClass (cipherText);
    outVehicle3.writeObject(print4);
}  catch (Throwable e) {
    // TODO Auto-generated catch block
    tv.append("\nUnable to perform RSA encryption!");
    e.printStackTrace();
}
