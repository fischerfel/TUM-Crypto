package com.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadFileByteByByte2 {

   public static void main(String[] args) throws Exception {

      InputStream inStream = null;
      BufferedInputStream bis = null;

      try{
         inStream = new FileInputStream("C:\\aa.mp4");

         bis = new BufferedInputStream(inStream);

         int numByte = bis.available();

         System.out.println(numByte/1024);
         ArrayList<byte[]> a = new ArrayList<>();
         ArrayList<byte[]> b = new ArrayList<>();
         byte[] buf = new byte[numByte];
         int ii=0;
         while(bis.read(buf, ii, 1024)!=-1){
                 a.add(buf);
         }
         System.out.println(a.size());
         for(int i=a.size()-1,j=-1;i>=0;i--,j++){
             MessageDigest digest = MessageDigest.getInstance("SHA-256");
             if(i==a.size()-1){
                 System.out.println(a.get(i).toString());
                 byte[] hash = digest.digest(a.get(i));
                 byte[] dest = new byte[a.get(i).length+hash.length];
                 System.arraycopy(a.get(i-1), 0, dest, 0, a.get(i-1).length);
                 System.arraycopy(hash, 0, dest, a.get(i-1).length, hash.length);
                 b.add(dest);
             }
             else{
                 System.out.println(i);
                 byte[] hash = digest.digest(b.get(0));
                 if(i!=0){
                     byte[] dest = new byte[a.get(i-1).length+hash.length];
                     System.arraycopy(a.get(i-1), 0, dest, 0, a.get(i-1).length);
                     System.arraycopy(hash, 0, dest, a.get(i-1).length, hash.length);
                     b.clear();
                     b.add(dest);
                 }else{
                 System.out.println(bytesToHex(hash));}
             }

         }

         }catch(Exception e){
            e.printStackTrace();
         }finally{
            if(inStream!=null)
               inStream.close();
            if(bis!=null)
               bis.close();
      } 
   }
   final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
   public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
