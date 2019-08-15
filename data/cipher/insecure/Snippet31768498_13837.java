decryption algorithm

    public static byte[] decrypt(byte[] ciphertext,byte[] keyBytes)   
    {   
        byte de[] = null;   
        try   
        {   
            SecureRandom sr = new SecureRandom(keyBytes);
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            kg.init(sr);
            SecretKey sk = kg.generateKey();    
            Cipher deCipher = Cipher.getInstance("RC4");   
            deCipher.init(Cipher.DECRYPT_MODE,sk);   
            de = deCipher.doFinal(ciphertext);   
        }   
        catch(Exception e)   
        {   
            e.printStackTrace();   
        }    
        return de;   

    }  
