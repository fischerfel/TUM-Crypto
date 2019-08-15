    CipherInputStream cis; 
    FileOutputStream fos = null;
    FileInputStream fis = null;

    File dataFile = new File(dataDir,fileName); // here I am getting encrypted file from server
    File newDataFile = new File(dataDir,fileName+"_TEMP"); // I am creating temporary decrypted file

    byte key[] = "abcdEFGH".getBytes();   
    SecretKeySpec secretKey = new SecretKeySpec(key,"DES"); 

    Cipher decrypt =  Cipher.getInstance("DES/ECB/PKCS5Padding");  
    decrypt.init(Cipher.DECRYPT_MODE, secretKey);  

    try {         
       fis = new FileInputStream(dataFile);
    } catch(Exception e) {  
        //Exception
    }  

    if(dataFile.exists()){
        cis = new CipherInputStream(fis,decrypt);  
        try {
            fos = new FileOutputStream(newDataFile);  
              byte[] b = new byte[8];  
          int i;
              while ((i=cis.read(b)) != -1) {  
                  fos.write(b, 0, i);  
             }                
            return newDataFile;
        } finally{
            try {
                if(fos != null)
                {
                 fos.flush();  
                 fos.close();                   }
                 cis.close();  
                 fis.close(); 
            } catch (IOException e) {
                //IOException
            }
        }
    }
