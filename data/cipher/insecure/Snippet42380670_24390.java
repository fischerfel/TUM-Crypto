        KeyGenerator keygen = KeyGenerator.getInstance("DES");
        SecretKey key = keygen.generateKey();
        cWrp.init(Cipher.WRAP_MODE, theirKey);
        byte[] m4 = cWrp.wrap(key);
        datOut.write(m4);
        ks = key;
        try{
            Cipher desCipher = Cipher.getInstance("DES");
            desCipher.init(Cipher.DECRYPT_MODE, ks);
        }
        catch(NoSuchPaddingException|InvalidKeyException e){
            System.out.println("Error: " + e);
        }
