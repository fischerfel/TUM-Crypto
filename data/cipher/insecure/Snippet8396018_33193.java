    FileOutputStream fos = null ;  
     CipherInputStream cis; 

     byte key[] = "abcdEFGH".getBytes();   
     SecretKeySpec secretKey = new SecretKeySpec(key,"DES"); 

     Cipher encrypt =  Cipher.getInstance("DES/ECB/PKCS5Padding");  
     encrypt.init(Cipher.ENCRYPT_MODE, secretKey);  

     InputStream fis = new ByteArrayInputStream(fileData);//Here I am getting file data as byte array. You can convert your file data to InputStream  by other way too.

    File dataFile = new File(dataDir,fileName); //dataDir is location where my file is stored
    if(!dataFile.exists()){
        cis = new CipherInputStream(fis,encrypt);  
        try {
            fos = new FileOutputStream(dataFile);  
              byte[] b = new byte[8];  
              int i;
              while ((i=cis.read(b)) != -1) {  
                  fos.write(b, 0, i);  
             }                
            return fileName;
        } finally{
            try {
                if(fos != null)
                {
                 fos.flush();  
                 fos.close();  
                }
                 cis.close();  
                 fis.close(); 
            } catch (IOException e) {
                //IOException
            }
        }
    }              
    return "";
