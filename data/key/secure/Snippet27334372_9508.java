public boolean clientValidate(SealedObject sealedClientReply, String loggedInKey) throws Exception{ 

    String encryptionKey = loggedInKey;
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

    Cipher dcipher = Cipher.getInstance("AES");
    dcipher.init(Cipher.DECRYPT_MODE, key);

    sealedClientReply.getObject(dcipher); // Server decrypts object
    System.out.println("I received this from the client: "+sealedClientReply);
    String decryptedClientReply= (String) sealedClientReply.getObject(dcipher);
    System.out.println("This is what I received decrypted: "+decryptedClientReply);


    //if(decryptedClientReply.equals(randString)){
    //  return true;
    //}else{
    //  return false;
    //}
    return true;
}
