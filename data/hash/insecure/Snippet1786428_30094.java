import java.io.*;
import java.security.MessageDigest;

public class Checksum 
{    
    const string Algorithm = "SHA-1"; // or MD5 etc.

    public static byte[] createChecksum(String filename) throws
       Exception
    {
        InputStream fis =  new FileInputStream(filename);
        try
        {
             byte[] buffer = new byte[1024];
             MessageDigest complete = MessageDigest.getInstance("MD5"); 
             int numRead;
             do 
             {
                 numRead = fis.read(buffer);
                 if (numRead > 0) 
                 {
                     complete.update(buffer, 0, numRead);
                 }
             } while (numRead != -1);
             return complete.digest();
         }
         finally
         {
             fis.close();
         }
     }
}
