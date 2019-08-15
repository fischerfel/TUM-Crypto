public String Encrypt(String text, String pubkey) {
        String encryptedText;
        PublicKey publicKey = null;
        Cipher cipher;
        try {
            publicKey = getPublicKeyFromString(pubkey);
            if (publicKey!=null) {
                if(text != null){
                    byte[] plainText = text.getBytes();
                    cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                    encryptedText = bASE64Encoder.encode(cipher.doFinal(plainText));
                }
                else{
                    encryptedText =  "Error@Encrypt: Null data received";
                }
            }
            else{
                encryptedText =  "Error@Encrypt: Public Key not found";
            }
        } 
        catch (Exception e) {
            encryptedText =  "Error@Encrypt: "+ e.getMessage();
        }
        return encryptedText;

    }
