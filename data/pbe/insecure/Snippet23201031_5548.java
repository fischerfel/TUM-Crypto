private void findMeaning(HttpServletResponse resp,String plainText) throws NoSuchAlgorithmException, 
    InvalidKeySpecException, 
    NoSuchPaddingException, 
    InvalidKeyException,
    InvalidAlgorithmParameterException, 
    UnsupportedEncodingException, 
    IllegalBlockSizeException, 
    BadPaddingException, 
    IOException{ 
        String text = plainText;
        String key="ezeon8547";
        KeySpec keySpec = new PBEKeySpec(key.toCharArray(), salt, iterationCount);//working
        SecretKey key1 = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);    //working    
         // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        ecipher = Cipher.getInstance(key1.getAlgorithm());//working
        ecipher.init(Cipher.ENCRYPT_MODE, key1, paramSpec);//working      
        String charSet="UTF-8";       
        byte[] in = text.getBytes(charSet);//working
        byte[] out = ecipher.doFinal(in);//working

        String encStr=new sun.misc.BASE64Encoder().encode(out);//unknown error
          sendResponse(resp, "Pincode city:" +encStr);//not get output
    }
