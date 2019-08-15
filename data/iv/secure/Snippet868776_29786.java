public AESFileDecrypter(String keyStr){
    try {
            Security.addProvider(new BouncyCastleProvider());   
            convertIvParameter();
            key = new sun.misc.BASE64Decoder().decodeBuffer(keyStr);

            //use the passed in Base64 decoded key to create a key object
            decryptKey = new SecretKeySpec(key, "AES");

            //specify the encryption algorithm
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

            //make a parameter object for the initialization vector(IV)             
            IvParameterSpec ivs = new IvParameterSpec(_defaultIv);

            //initialize the decrypter to the correct mode, key used and IV
            decryptCipher.init(Cipher.DECRYPT_MODE, decryptKey, ivs);    
        } 
     catch (Exception e) {
             e.printStackTrace();
     } 
}

public void convertIvParameter() {

   int[] iv = new int[] {11, 190, 165, 33, 68, 88, 11, 200, 245, 35, 68, 23, 60, 24, 223, 67};

   _defaultIv = new byte[16];

   for(int x = 0; x < _defaultIv.length; x++) {
      _defaultIv[x] = (byte)iv[x];
   }
}

public void decryptUpdate(byte[] inpBytes) throws Exception {
   //decrypt the byte passed in from the web server
   decryptCipher.update(inpBytes);  
}

public byte[] decryptFinal() throws Exception {
   //decrypt the byte passed in from the web server
   return decryptCipher.doFinal();
}

//sends bytes to the client for diaply
private void sendBytes(FileInputStream fis, OutputStream os)throws Exception {
    //set the buffer size to send 4k segments of data
aesFileDecrypter = new AESFileDecrypter(<Insert Key string here>);

    byte[] buffer = new byte[4096];
    int bytes = 0, totalBytes = fis.available();

    //while there is still data to be sent keep looping and write the data
    //to the output stream as the buffer is filled
    try {
       while ((bytes = fis.read(buffer)) != -1) {   
          aesFileDecrypter.decryptUpdate(buffer);
          //os.write(buffer, 0, bytes);
       }

       os.write(aesFileDecrypter.decryptFinal(), 0, totalBytes);
   }
   catch(Exception e) {
      e.printStackTrace();
   }
}
