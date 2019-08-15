import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class RC2Encrypt
{
    public static void main(String args []) throws Exception
    {
        Scanner s=new Scanner(System.in);
        System.out.println("Enter PlainTextString:");
        String input=s.nextLine();

        System.out.println();
        System.out.println("Enter 16 digit key:");
        String strPassword=s.nextLine();

        SecretKeySpec key = new SecretKeySpec(strPassword.getBytes(), "RC2");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(strPassword.getBytes());
        Cipher cipher =  Cipher.getInstance("RC2");
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

        byte[] encrypted = cipher.doFinal(input.getBytes());

        String b1 = new String(encrypted);
        System.out.println("Original string: " + input);
        System.out.println("Encrypted string: " + b1);
    }
}
