import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class CryptoHash {
  public static void main( String[] args ) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance( "SHA-256" );
    String text = "Text to hash, cryptographically.";

    // Change this to UTF-16 if needed
    md.update( text.getBytes( StandardCharsets.UTF_8 ) );
    byte[] digest = md.digest();

    String hex = String.format( "%064x", new BigInteger( 1, digest ) );
    System.out.println( hex );
  }
}
