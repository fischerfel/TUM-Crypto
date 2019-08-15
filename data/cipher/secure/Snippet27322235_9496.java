@WebMethod
public byte[] getSymmetricKey(){
    try{
        Cipher cipher = Cipher.getInstance("RSA");

        // First, encrypts the symmetric key with the client's public key
        cipher.init(Cipher.ENCRYPT_MODE, this.clientKey);
        byte[] partialCipher = cipher.doFinal(this.key.getBytes());

        // Finally, encrypts the previous result with the server's private key
        cipher.init(Cipher.ENCRYPT_MODE, this.privateKey);
        byte[] cipherData = cipher.doFinal(partialCipher);

        return cipherData;
    }catch (Exception ex){
        ex.printStackTrace();
    }

}
