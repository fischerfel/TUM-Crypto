import java.math.BigInteger;
import java.security.MessageDigest;

public class Test
{
    public static void main(String[] args) throws Exception
    {
        byte[] bytes = { 0x35, 0x24, 0x76, 0x12 };
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] digest = m.digest(bytes);
        String hash = new BigInteger(1, digest).toString(16);
        System.out.println(hash);
    }
}
