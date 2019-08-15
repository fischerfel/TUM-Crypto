public byte[] mySign(byte[] aMessage){
    try{
        // get an instance of a cipher with RSA with ENCRYPT_MODE
        // Init the signature with the private key
          Cipher cipher = Cipher.getInstance("RSA");
          cipher.init(Cipher.ENCRYPT_MODE, this.thePrivateKey);

        // get an instance of the java.security.MessageDigest with sha1
             MessageDigest meassDs = MessageDigest.getInstance("SHA-1");

        // process the digest
             meassDs.update(aMessage);
             byte[] digest = meassDs.digest();

            byte [] signature = cipher.doFinal(digest);

        // return the encrypted digest
        return signature;

    }catch(Exception e){
        System.out.println(e.getMessage()+"Signature error");
        e.printStackTrace();
        return null;
    }

}



public boolean myCheckSignature(byte[] aMessage, byte[] aSignature, PublicKey aPK){
    try{
        // get an instance of a cipher with RSA with DECRYPT_MODE
        // Init the signature with the public key
          Cipher cipher = Cipher.getInstance("RSA");
          cipher.init(Cipher.DECRYPT_MODE, aPK);

        // decrypt the signature
             byte [] digest1 = cipher.doFinal(aSignature);

        // get an instance of the java.security.MessageDigest with sha1
             MessageDigest meassDs = MessageDigest.getInstance("SHA-1");

        // process the digest
             meassDs.update(aMessage);
             byte[] digest2 = meassDs.digest();

        // check if digest1 == digest2
        if (digest1 == digest2)
        return true;
        else
            return false;

    }catch(Exception e){
        System.out.println("Verify signature error");
        e.printStackTrace();
        return false;
    }
}   
