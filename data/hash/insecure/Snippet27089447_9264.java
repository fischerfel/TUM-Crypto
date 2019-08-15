import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class MD5 {

    public static String getMD5Sum(String filePath) throws Exception{

        MessageDigest md = MessageDigest.getInstance("MD5");

        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
          DigestInputStream dis = new DigestInputStream(is, md);
          int read = 0;
          do{
              read = dis.read();
          }while(read > -1);
        }
        byte[] digest = md.digest();
        digest.toString();
         String result = "";

           for (int i=0; i < digest.length; i++) {
               result += Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1     );
           }
           return result;
    }
}
