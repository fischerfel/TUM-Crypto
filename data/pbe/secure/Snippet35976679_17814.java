    public String encrypt(String secretKey, String plainText) 
            throws NoSuchAlgorithmException, 
            InvalidKeySpecException, 
            NoSuchPaddingException, 
            InvalidKeyException,
            InvalidAlgorithmParameterException, 
            UnsupportedEncodingException, 
            IllegalBlockSizeException, 
            BadPaddingException{
        //Key generation for enc and desc
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);        
         // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        //Enc process
        ecipher = Cipher.getInstance(key.getAlgorithm());
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);      
        String charSet="UTF-8";       
        byte[] in = plainText.getBytes(charSet);
        byte[] out = ecipher.doFinal(in);
        String encStr = new Base64.encodeBase64String(out);

        return encStr;
    }

     public String decrypt(String secretKey, String encryptedText)
     throws NoSuchAlgorithmException, 
            InvalidKeySpecException, 
            NoSuchPaddingException, 
            InvalidKeyException,
            InvalidAlgorithmParameterException, 
            UnsupportedEncodingException, 
            IllegalBlockSizeException, 
            BadPaddingException, 
            IOException{
         //Key generation for enc and desc
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);        
         // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        //Decryption process; same key will be used for decr
        dcipher=Cipher.getInstance(key.getAlgorithm());
        dcipher.init(Cipher.DECRYPT_MODE, key,paramSpec);
        byte[] enc = new Base64.decodeBase64(encryptedText);
        byte[] utf8 = dcipher.doFinal(enc);
        String charSet="UTF-8";     
        String plainStr = new String(utf8, charSet);
        return plainStr;
    }    
