private byte[] encryptMessage(Key key, byte[] data){
    Cipher cipher;
    if(key instanceof PrivateKey || key instanceof PublicKey){
        //RSA key
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to encrypt message");
            e.printStackTrace();
        }


    }else if(key instanceof SecretKey){
        //AES secret key
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }else{
        System.out.println("Invalid key type");
    }
    return null;
}
