private void findMeaning(HttpServletResponse resp,String plainText) throws MalformedURLException, NoSuchAlgorithmException, 
    InvalidKeySpecException, 
    NoSuchPaddingException, 
    InvalidKeyException,
    InvalidAlgorithmParameterException, 
    UnsupportedEncodingException, 
    IllegalBlockSizeException, 
    BadPaddingException{ 





        /**
         * 
         * @param secretKey Key used to encrypt data
         * @param plainText Text input to be encrypted
         * @return Returns encrypted text
         * 
         */
        String secretKey = "ezeon8547";


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
            String encStr=new sun.misc.BASE64Encoder().encode(out);

              sendResponse(resp, "Pincode city:" +encStr);

         /**     
         * @param secretKey Key used to decrypt data
         * @param encryptedText encrypted text input to decrypt
         * @return Returns plain text after decryption
         */



    }
