import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class AuthTest
{
    public static void main (String[] args) throws java.lang.Exception
    {
        byte[] result;
        byte[] salt = new byte[] { (byte)0xe3, (byte)0x2c, (byte)0xf8, (byte)0x9e, (byte)0x6f, (byte)0xe4, (byte)0xf8, (byte)0x90 };
        byte[] password = "password".getBytes("UTF-8");

        result = getHash(1105, password, salt);
        System.out.println(result[0]);
    }

    public static byte[] getHash(int iterations, byte[] password, byte[] salt) throws NoSuchAlgorithmException,
        UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.reset();
        md.update(salt);
        byte[] result = md.digest(password);
        for (int i = 0; i < iterations; i++) {
            md.reset();
            result = md.digest(result);
        }
        return result;
    }
}
