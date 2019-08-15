    CipherInputStream cis;
    String salt = "1234567890123456";
    String password = "abcdEFGH";

    password = password.concat(salt);
    String validpassword = password.substring(0, 16);          
    SecretKeySpec secretKey =new SecretKeySpec(validpassword.getBytes(),"AES");        
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(salt.getBytes());

     try {
         // Creation of Cipher objects
         Cipher decrypt = 
              Cipher.getInstance("AES/CFB8/NoPadding");
         decrypt.init(Cipher.DECRYPT_MODE, secretKey,paramSpec); 

         // Open the Encrypted file
         cis = new CipherInputStream(is, decrypt); 

         int bytesRead;
         int current = 0;
         byte[] b = new byte[256];
         bytesRead = cis.read(b,0,256);
