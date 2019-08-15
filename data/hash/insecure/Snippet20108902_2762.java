import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

public class JavaTest {
    public static void main (String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String rawString = "9498131529";
        System.out.println(Charset.defaultCharset());
        System.out.println(rawString);

        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(rawString.getBytes("UTF-8")); 
        BASE64Encoder encoder = new BASE64Encoder();
        byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
        System.out.println(encoder.encode(hashedBytes));
    }
}
