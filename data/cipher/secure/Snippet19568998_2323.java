    public class DecryptFile {  
  public static File main(String args[], File encFile, Context context) { 
      for (int i = 0; i < args.length; i++) {
        Log.i("ARGS", args[i]);
    }
       try {  
            File aesFile = new File(args[0]);  

            aesFile= encFile;  
            Log.d("AESFILELENGTH", "aes length: " + aesFile.length());
            File aesFileBis = new File(context.getFilesDir(), args[0]);

            FileInputStream fis;  
            FileOutputStream fos;  
            CipherInputStream cis;  

 //Creation of Secret key  
            String key = "mysecretkey";  
            int length=key.length();  
            if(length>16 && length!=16){  
                 key=key.substring(0, 15);  
            }  
            if(length<16 && length!=16){  
                 for(int i=0;i<16-length;i++){  
                      key=key+"0";  
                 }  
            }  
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(),"AES");  
             //Creation of Cipher objects  
            Cipher decrypt =Cipher.getInstance("AES/CBC/PKCS5Padding");  

            byte[] aByte = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


            IvParameterSpec ivSpec = new IvParameterSpec(aByte);

            decrypt.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);  

 // Open the Encrypted file  

            fis = new FileInputStream(aesFile);  

            cis = new CipherInputStream(fis, decrypt);  

            // Write to the Decrypted file  
            fos = new FileOutputStream(aesFileBis);  
            try {

                byte[] mByte = new byte[8];  
                int i = cis.read(mByte); 
                Log.i("MBYTE", "mbyte i: " + i);
                while (i != -1) {  
                    fos.write(mByte, 0, i);  
                    i = cis.read(mByte);  
                }  
            } catch (IOException e) {
                e.printStackTrace();
            }


            fos.flush();  
            fos.close();  
            cis.close();  
            fis.close(); 
            return aesFileBis;
       } catch(Exception e){  
            e.printStackTrace();  
       }
    return null;  
  }  
}
