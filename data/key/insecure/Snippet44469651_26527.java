 byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
 String key = "1234567890123456789012345678901d";

 AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
 SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
 cipher.init(mode, newKey, ivSpec);

 InputStream input = null;
 OutputStream output = null;

 try {
     input = new BufferedInputStream(new FileInputStream(new File("/home/java/test/aaa.JPG")));
     output = new BufferedOutputStream(new FileOutputStream(new File("/home/java/test/bbb.JPG")));
     byte[] buffer = new byte[1024];
     int read = -1;

     while((read = input.read(buffer)) != -1){
         output.write(cipher.update(buffer, 0, read));
     }

      output.write(cipher.doFinal());
 }
 finally {
     if(output != null){
         try {
             output.close();
         } catch(IOException ie){
             logger.info(ie.getMessage());
         }
     }
     if(input != null){
         try {
             input.close();
         } catch(IOException ie){
             logger.info(ie.getMessage());
         }
     }
 }
