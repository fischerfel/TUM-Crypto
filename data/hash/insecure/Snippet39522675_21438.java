import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;

public class md5test {
    public static void main(String[] args) throws Exception {

        String yourString = "Test";
        byte[] bytesOfMessage = yourString.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);

        byte[] encodedBytes = Base64.encodeBase64(thedigest);
        System.out.println("encodedBytes " + new String(encodedBytes));
    }
}
