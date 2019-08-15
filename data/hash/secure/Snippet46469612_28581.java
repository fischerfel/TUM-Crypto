import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
public class MyClass {
    public static void main(String args[]) {
        String test = "line1\nline2";
        System.out.println(test);
        String testOut = getTest(test);
        System.out.println(testOut);
} 

public static String getTest(String input) {
try {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(input.getBytes());
    return Base64.getEncoder().encodeToString(md.digest());
    } 
    catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}      
}
