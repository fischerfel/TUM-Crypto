   import java.security.*;
   import java.util.*;
   import java.math.*;
   import java.io.*;

   public class MD5 {
       private MessageDigest   mDigest;
       private File            openFile;
       private FileInputStream ofis;
       private int             fSize;  
       private byte[]          fBytes;

       public MD5(String filePath) {
           try { mDigest = MessageDigest.getInstance("MD5"); } 
           catch (NoSuchAlgorithmException e) { System.exit(1); }
           openFile = new File(filePath); 
       }   
       public String toString() {
           try {
               ofis    = new FileInputStream(openFile);
               fSize   = ofis.available();
               fBytes  = new byte[fSize];
               ofis.read(fBytes);
           } catch (Throwable t) {
               return "Can't read file or something";
           }  

           mDigest.update(fBytes);
           return new BigInteger(1, mDigest.digest()).toString(16);
       }  
       public static void main(String[] argv){
           MD5 md5 =  new MD5("someFile.ext");
           System.out.println(md5);
       }  
   } 
