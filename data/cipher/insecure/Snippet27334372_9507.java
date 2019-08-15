public boolean validateClient(String loggedInKey) throws Exception{

    /* Testing */
    String randString = this.aI.returnString(); // Client requests a string/ server returns
    System.out.println("This is what the server gave me: "+randString);

    String encryptionKey = loggedInKey;
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

    Cipher ecipher = Cipher.getInstance("AES");
    ecipher.init(Cipher.ENCRYPT_MODE, key);

    SealedObject sealedClientReply = new SealedObject(randString, ecipher); // Client encrypts returned string seals it
    System.out.println("This is what I'm sending back: "+sealedClientReply);
    if(this.aI.clientValidate(sealedClientReply, loggedInKey)){
        System.out.println("Client validated!");
        return true;
    }else{
        return false;
    }       
}
