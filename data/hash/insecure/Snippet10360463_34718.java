import java.security.*;
import java.math.*;

public class b {
public static void main(String args[]) throws Exception{
    String s="Hello";
    MessageDigest m=MessageDigest.getInstance("MD5");
    m.update(s.getBytes(),0,s.length());
    String originalHash = new BigInteger(1,m.digest()).toString(16);
    System.out.println("MD5: " + originalHash);

    for (long i = 0; i < 9223372036854775807L; i++)
    {
        String iString = i + "";
        m.update(iString.getBytes(),0,iString.length());
        iString = new BigInteger(1,m.digest()).toString(16);
        if (originalHash.equals(iString))
        {
            System.out.println("Found MD5: " + iString);
            break;
        }
        if (i%1000000 == 0)
        {
            System.out.println("Count: " + (long)i/1000000 + "M");
            System.out.println("Sample Hash: " + iString);
        }
    }
}
}
