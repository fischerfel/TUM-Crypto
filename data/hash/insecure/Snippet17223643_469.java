//writer:
import java.util.*;
import java.io.*;
import java.security.*;

public class Writer{
    public static void main(String []args)throws Exception{


        InputStream  is =  new FileInputStream("input.txt");
        PrintStream os=new PrintStream(new File("out.txt"));
        byte[] buffer = new byte[1024];
        String str;
        MessageDigest complete = MessageDigest.getInstance("SHA-1");
        MessageDigest partial = MessageDigest.getInstance("SHA-1");
        int numRead;
        do {
             numRead = is.read(buffer);
             if (numRead > 0) {
                complete.update(buffer, 0, numRead);
                partial.update(buffer,0,numRead);

                byte []digest=partial.digest();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {

                    sb.append(String.format("%x", digest[i]));
                }
                System.out.println(sb.toString());
                str=new String(digest);
                os.println(str);
                partial.reset();
            }
        } while (numRead != -1);

        byte []digest=complete.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {

           sb.append(String.format("%x", digest[i]));
        }
       System.out.println(sb.toString());
        str=new String(digest);
        os.println(str);


       is.close();
       os.close();
    }    
}
