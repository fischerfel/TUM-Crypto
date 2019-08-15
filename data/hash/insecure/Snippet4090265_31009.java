import java.util.Random;
import java.security.*;
import java.math.*;

public class RandPassGen {
    public static String genPass( int chars ) {
        Random r = new Random();
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch ( NoSuchAlgorithmException e ) {
            System.out.println( "Unsupported Algorithm!" );
            return null;
        }

        byte[] entropy = new byte[1024];
        r.nextBytes(entropy);
        md.update( entropy , 0, 1024 );

        return new BigInteger(1, md.digest()).toString(16).substring(0, chars);
    }

    public static void main( String[] av ) {
        Integer chars = Integer.valueOf(av[0]);
        if ((chars < 0) || (chars > 32)) {
            System.out.println( "Generate between 0 and 32 characters." );
            return;
        }

        System.out.println( genPass( chars ) ); 
    }
}
