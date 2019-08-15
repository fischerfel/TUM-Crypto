import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Test {
    public static void main(String[] args) {
        String inputOne = "abc";
        String a = new String(toHash(inputOne));
        System.out.println("hash with salt of String a:" + a);
        String inputTwo = "abc";
        String b = new String(toHash(inputTwo));
        System.out.println("hash with salt of String b:" + b);
        if(Arrays.equals(toHash(inputOne), toHash(inputTwo))) {
            System.out.println("Same");
        }
        else {
            System.out.println("Not Same");
        }
    }

    public static byte[] toHash(String password) {
        byte[] salt = new byte[16];
        byte[] hash = null;
        SecretKeyFactory f = null;
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            hash = f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hash;
    }
}
