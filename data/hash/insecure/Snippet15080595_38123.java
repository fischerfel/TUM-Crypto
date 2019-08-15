import java.security.MessageDigest;
import java.math.BigInteger; 

public class Hash 
{ 
   public static void main( String[] args ) throws Exception 
   { 
       MessageDigest md5    = MessageDigest.getInstance("MD5");
       String        plain  = "abcd1234"; 
       BigInteger    digest = new BigInteger(md5.digest(plain.getBytes("UTF-8"))); 
       System.out.println( digest.abs() ); 
   } 
} 
