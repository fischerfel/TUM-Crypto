        byte[] m4 = new byte[256];
        datIn.read(m4);
        cUwp.init(Cipher.UNWRAP_MODE, myKey);
        ks = (SecretKey)cUwp.unwrap(m4, "DES", Cipher.SECRET_KEY);
        System.out.println("Recieved key:\n" + ks);
        try{
            Cipher desCipher = Cipher.getInstance("DES");
            desCipher.init(Cipher.DECRYPT_MODE, ks);
        }
        catch(NoSuchPaddingException|InvalidKeyException e){
            System.out.println("Error: " + e);
        }
