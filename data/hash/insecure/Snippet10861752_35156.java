import java.security.MessageDigest;    
import java.security.NoSuchAlgorithmException;    
import sun.misc.BASE64Encoder;

public class NewClass {       
 public static void main(String args[]) throws NoSuchAlgorithmException        
 {       
    MessageDigest digest = MessageDigest.getInstance("SHA-1");    
    System.out.println("Algorithm :"+digest.getAlgorithm());    
    digest.update("welcome".getBytes());    
    byte[] result = digest.digest();     
    hash = (new BASE64Encoder()).encode(result);     
    MessageDigest digest1 = MessageDigest.getInstance("SHA-1");    
    digest1.update("welcome".getBytes());    
    byte[] result1 = digest1.digest();    
    System.out.println(result);    
    System.out.println(result1);    
    String hash1 = (new BASE64Encoder()).encode(result);    
    System.out.println("Digest value"+hash);    
    System.out.println("Digest value"+hash1);       
 }

}
