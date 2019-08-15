import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import java.util.Base64.Encoder;;

public class HashTextTest {

public static void main(String[] args) throws NoSuchAlgorithmException {
    System.out.println(sha1(""));  
}
   static String sha1(String input) throws NoSuchAlgorithmException {
    MessageDigest mDigest = MessageDigest.getInstance("SHA1");
    byte[] result = mDigest.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
        sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
}
}
