 public String Decrypt(String text, String pubkey,PrivateKey privatekey ) {
        KeyStore keyStoreBrowser = null;
        String decryptedString;
        Cipher cipher = null;
        byte[] encryptText= new byte[256];
        try {
            keyStoreBrowser =  KeyStore.getInstance("Windows-MY");//, "SunMSCAPI");
            keyStore.load(null, null);
            String msg=null;
            if (keyStoreBrowser != null) {
                    if (privatekey != null) {
                        if (text != null) {
                            encryptText = this.bASE64Decoder.decodeBuffer(text);
                            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",keyStoreBrowser.getProvider());
                            cipher.init(Cipher.DECRYPT_MODE, privatekey);
                            decryptedString = new String(cipher.doFinal(encryptText));
                        } 
                    } 
                }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println( e.getMessage());
        }
        return decryptedString;
    }
