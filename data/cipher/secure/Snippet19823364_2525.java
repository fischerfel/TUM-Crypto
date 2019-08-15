import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.*;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptor {

private String algo;
private File file;

public FileEncryptor(String algo,String path) {
this.algo=algo; //setting algo
this.file=new File(path); //settong file
}

 public void encrypt() throws Exception{
    //opening streams
     FileInputStream fis =new FileInputStream(file);
     file=new File(file.getAbsolutePath());
     FileOutputStream fos =new FileOutputStream(file);
     //generating key
     byte k[] = "HignDlPs".getBytes();   
     SecretKeySpec key = new SecretKeySpec(k,algo.split("/")[0]);  
     //creating and initialising cipher and cipher streams
     Cipher encrypt =  Cipher.getInstance(algo);  
     encrypt.init(Cipher.ENCRYPT_MODE, key);  
     CipherOutputStream cout=new CipherOutputStream(fos, encrypt);

     byte[] buf = new byte[1024];
     int read;
     while((read=fis.read(buf))!=-1)  //reading data
         cout.write(buf,0,read);  //writing encrypted data
     //closing streams
     fis.close();
     cout.flush();
     cout.close();
 }

 public static void main (String[] args)throws Exception {
     new FileEncryptor("DES/ECB/PKCS5Padding","C:\\Users\\*******\\Desktop\\newtext").encrypt();//encrypts the current file.
  }
}
