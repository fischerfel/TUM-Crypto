public String Decrypt(String text, String pubkey) {
        PrivateKey privatekey = null;
        KeyStore keyStoreBrowser = null;
        String decryptedString;
        Cipher cipher = null;
        byte[] encryptText;
        try {
            keyStoreBrowser = initializeBrowserKeyStore();
            if(keyStoreBrowser != null) {
                privatekey = getPrivateKeyFromKeyStore(pubkey, keyStoreBrowser);
                if(privatekey != null) {
                    if(text != null){
                        encryptText = this.bASE64Decoder.decodeBuffer(text);
                        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                        cipher.init(Cipher.DECRYPT_MODE, privatekey);
                        decryptedString = new String(cipher.doFinal(encryptText));
                    }
                    else{
                        decryptedString =  "Error@Decrypt: Null data received to decrypt.";
                    }
                }
                else{
                    printMessageToConsole("Private is null");
                    decryptedString =  "Error@Decrypt: Private Key Not Found.";
                }
            }
            else{
                printMessageToConsole("KeyStore  not found");
                decryptedString =  "Error@Decrypt: KeyStore is null.";
            }
        }
        catch (Exception e) {
            decryptedString = "Error@Decrypt:"+ e.getMessage();
        }
        return decryptedString;
    }
