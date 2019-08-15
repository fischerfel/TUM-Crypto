import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static jdk.nashorn.tools.ShellFunctions.input;

public class Sha256hash 
{

    public static String generate(String input) throws NoSuchAlgorithmException 
    {
        MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();

            String hexStr = "";
            for (int i = 0; i < digest.length; i++) {
                hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
            }
            return hexStr;
    }

}
