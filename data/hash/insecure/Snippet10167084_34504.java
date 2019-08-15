package HashCracker;

import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cracker {
    // Array of chars used to produce strings
    public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();    
    public static final int numOfChar=40;

    /*
     Given a byte[] array, produces a hex String,
     such as "234a6f". with 2 chars for each byte in the array.
     (provided code)
    */
    public static String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int i=0; i<bytes.length; i++) {
            int val = bytes[i];
            val = val & 0xff;  // remove higher bits, sign
            if (val<16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    /*
     Given a string of hex byte values such as "24a26f", creates
     a byte[] array of those values, one byte value -128..127
     for each 2 chars.
     (provided code)
    */
    public static byte[] hexToArray(String hex) {
        byte[] result = new byte[hex.length()/2];
        for (int i=0; i<hex.length(); i+=2) {
            result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
        }
        return result;
    }


    public static void main(String args[]) throws NoSuchAlgorithmException
    {

        if(args.length==1)//Hash Maker
        {
            //create a byte array , meassage digestand put password into it 
            //and get out a hash value printed to the screen using provided methods. 
            byte[] myByteArray=args[0].getBytes();
            MessageDigest hasher=MessageDigest.getInstance("SHA-1");
            hasher.update(myByteArray);
            byte[] digestedByte=hasher.digest();
            String hashValue=Cracker.hexToString(digestedByte); 

            System.out.println(hashValue);
        }
        else//Hash Cracker
        {
            ArrayList<Thread> myRunnables=new ArrayList<Thread>();
            int numOfThreads = Integer.parseInt(args[2]);
            int charPerThread=Cracker.numOfChar/numOfThreads;
            int start=0;
            int end=charPerThread-1;

            for(int i=0; i<numOfThreads; i++)
            {
                //creates, stores and starts threads. 
                Runnable tempWorker=new Worker(start, end, args[1], Integer.parseInt(args[1]));
                Thread temp=new Thread(tempWorker);
                myRunnables.add(temp);
                temp.start();
                start=end+1;
                end=end+charPerThread;
            }


        }


    }





import java.util.*;

public class Worker implements Runnable{

    private int charStart;
    private int charEnd;
    private String Hash2Crack;
    private int maxLength;
    public Worker(int start, int end, String hashValue, int maxPWlength)
    {
        charStart=start;
        charEnd=end;
        Hash2Crack=hashValue;
        maxLength=maxPWlength;


    }



    public void run()
    {
        byte[] myHash2Crack_=Cracker.hexToArray(Hash2Crack);

        for(int i=charStart; i<charEnd+1; i++)
        {

            ////// this is where I am stuck.
        }



    }
}
