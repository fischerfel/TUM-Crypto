   `private static void EncFile(File srcFile, File encFile) throws Exception 
      {
           if(!srcFile.exists()){
            System.out.println("source file not exixt");
            return;
        }//
         if(!encFile.exists()){
           System.out.println("encrypt file created");
           encFile.createNewFile();
        }

    byte[] bytes = new byte[1024*8];   
    String key = "1234567812345678";
    String iv = "1234567812345678";
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    int blockSize = cipher.getBlockSize();
    SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
    IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
    cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);


    InputStream fis  = new FileInputStream(srcFile);
   // CipherInputStream cin = new CipherInputStream(fis, cipher);  
    OutputStream fos = new FileOutputStream(encFile);

   while ((dataOfFile = fis.read(bytes)) >0) {


       byte[] encrypted = cipher.doFinal(bytes);
        fos.write(encrypted,0,dataOfFile);
    }

    fis.close();
    fos.flush();
    fos.close();
}`
