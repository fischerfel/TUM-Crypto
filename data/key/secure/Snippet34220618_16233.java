 public String encryptDecryptAes(String key , String input , int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        java.security.Security.addProvider(new BouncyCastleProvider());
        String result = null;
        byte[] inputBytes = input.getBytes();
        byte[] keyBytes = key.getBytes() ;

        SecretKeySpec secretKey = new SecretKeySpec(keyBytes , "AES") ;

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        if(mode==0) {

            //Encrypt
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

             cipherText = new byte[cipher.getOutputSize(inputBytes.length)];
             ctLength = cipher.update(inputBytes, 0, inputBytes.length, cipherText, 0);
            ctLength += cipher.doFinal(cipherText, ctLength);
            result = new  String(cipherText);


        }

        if(mode==1) {

            //DECRYPT
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] output = new byte[cipher.getOutputSize(ctLength)];
            int ptLength = cipher.update(cipherText, 0, ctLength, output, 0);
            ptLength += cipher.doFinal(output, ptLength);
           result = new String(output);
        }

        return result ;
    }