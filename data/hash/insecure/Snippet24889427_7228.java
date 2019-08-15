import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Digest {
    /**
     * 
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {

        String userName = "superuser";
        String password = "superuser";

        byte[] userNameBytes = userName.getBytes(Charset.forName("GBK"));
        byte[] passwordBytes = password.getBytes(Charset.forName("GBK"));

        byte[] hashedBytes = digest(userNameBytes,passwordBytes);

        System.out.println(Arrays.toString(hashedBytes));

        String tmp = new String(hashedBytes,Charset.forName("GBK"));
        byte[] newHashedBytes = tmp.getBytes(Charset.forName("GBK"));

        System.out.println(Arrays.toString(newHashedBytes));
    }

    public static byte[] digest(byte[] username, byte[] password) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.reset();
        md.update(password);
        md.update(username);
        return md.digest();
    }
}
