import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
public class GenerateMD5 {
public static void main(String args[]) throws Exception{
    String s = "<CORSConfiguration> <CORSRule> <AllowedOrigin>http://www.example.com</AllowedOrigin> <AllowedMethod>PUT</AllowedMethod> <AllowedMethod>POST</AllowedMethod> <AllowedMethod>DELETE</AllowedMethod> <AllowedHeader>*</AllowedHeader> <MaxAgeSeconds>3000</MaxAgeSeconds> </CORSRule> <CORSRule> <AllowedOrigin>*</AllowedOrigin> <AllowedMethod>GET</AllowedMethod> <AllowedHeader>*</AllowedHeader> <MaxAgeSeconds>3000</MaxAgeSeconds> </CORSRule> </CORSConfiguration>";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(s.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        /*for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }*/
        System.out.println(sb.toString());
        StringBuffer sbi = new StringBuffer();
        byte [] bytes = Base64.encodeBase64(digest);
        String finalString = new String(bytes);
        System.out.println(finalString);
    }
}
