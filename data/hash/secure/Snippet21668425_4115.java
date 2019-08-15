package com.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadFileByteByByte {

    public static void main(String[] args) throws Exception {

        InputStream inStream = null;
        BufferedInputStream bis = null;

        try{
            inStream = new FileInputStream("C:\\a.mp4");

            bis = new BufferedInputStream(inStream);

            int numByte = bis.available();


            byte[] buf = new byte[numByte];
            bis.read(buf, 0, numByte);
            System.out.println(numByte/1024);
            ArrayList<byte[]> a = new ArrayList<>();
            ArrayList<byte[]> b = new ArrayList<>();
            for(int i=0,j=0;i<buf.length;i++,j++){
                byte[] buf2 = new byte[1057];
                buf2[j] = buf[i];
                if(i%1024==1023){
                    a.add(buf2);
                    j=0;
                }
            }

            for(int i=a.size()-1,j=-1;i>=0;i--,j++){
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                if(i==a.size()-1){
                    byte[] hash = digest.digest(a.get(i));
                    byte[] dest = new byte[a.get(i).length+hash.length];
                    System.arraycopy(a.get(i-1), 0, dest, 0, a.get(i-1).length);
                    System.arraycopy(hash, 0, dest, a.get(i-1).length, hash.length);
                    b.add(dest);
                }
                else{
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
