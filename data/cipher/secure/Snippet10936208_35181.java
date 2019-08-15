private byte[] decryptMessage(Key key, byte[] data){
    Cipher cipher;
    System.out.println("Decrypting message...");
    System.out.println("key: " + key.toString());
    System.out.println("data: " + data);
    System.out.println("data length: " + data.length);
    if(key instanceof PublicKey){
        //RSA public key
        System.out.println("Found PublicKey");
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to decrypt message");
            e.printStackTrace();
        }

    }else if(key instanceof PrivateKey){
        //RSA private key
        System.out.println("Found PrivateKey");
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] results = cipher.doFinal(data);
            System.out.println("Decrypted results: " + results);
            return results;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to decrypt message");
            e.printStackTrace();
        }
    }else if(key instanceof SecretKey){
        //AES secret key
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
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
