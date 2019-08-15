public void decrypt(Cipher c) throws ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException{

    //decrypting the AES key (CryptoStack.java:110)
    keyCache = (SecretKey) key.getObject(c);

    //generating Cipher for decryption
    Cipher c1 = null;
    try {
        c1 = Cipher.getInstance("AES");
        c1.init(Cipher.DECRYPT_MODE, keyCache);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    //decrypting Object with previous generated Cipher
    stackCache = (Stack) stack.getObject(c1);
}
