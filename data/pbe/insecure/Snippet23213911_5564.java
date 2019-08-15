private void findMeaning(HttpServletResponse resp,String encryptText) throws NoSuchAlgorithmException, 
    InvalidKeySpecException, 
    NoSuchPaddingException, 
    InvalidKeyException,
    InvalidAlgorithmParameterException, 
    UnsupportedEncodingException, 
    IllegalBlockSizeException, 
    BadPaddingException, 
    IOException, ShortBufferException, NoSuchProviderException{ 
       String text = encryptText;
        String secretKey="ezeon8547";

       KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
       SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);        
        // Prepare the parameter to the ciphers
       AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
       //Decryption process; same key will be used for decr
       dcipher=Cipher.getInstance(key.getAlgorithm());
       dcipher.init(Cipher.DECRYPT_MODE, key,paramSpec);
       byte[] enc = new sun.misc.BASE64Decoder().decodeBuffer(text);//possible problem here please resolve

       byte[] utf8 = dcipher.doFinal(enc);
       String charSet="UTF-8";     
       String plainStr = new String(utf8, charSet);

          sendResponse(resp, "Pincode city:" +plainStr);


    }}
