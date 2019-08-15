import java.security.MessageDigest; 
import org.jboss.security.Base64Encoder; 

public class EncryptPassword { 

  public static void main(String[] args) { 
    // TODO Auto-generated method stub 
    String algoritmo = "MD5"; 
    String clearTextPassword = "passwd123"; 
    String hashedPassword = null; 

    try { 
       byte[] hash = MessageDigest.getInstance(algoritmo).digest(clearTextPassword.getBytes()); 
       hashedPassword = Base64Encoder.encode(hash); 
       System.out.println("Clear Text Password : " + clearTextPassword); 
       System.out.println("Encrypted Password : " + hashedPassword); 
    } catch (Exception e) { 
       e.printStackTrace(); 
    } 
  } 
 } 
