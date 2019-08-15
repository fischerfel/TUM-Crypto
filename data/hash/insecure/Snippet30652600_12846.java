import java.security.MessageDigest;
public class test_SHA_java 
{
public static void main(String[] args)throws Exception
{
MessageDigest md = MessageDigest.getInstance("SHA-1");
String text = "This is some text";

md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
byte[] digest = md.digest();
 }
}
real    0m0.087s
user    0m0.080s
sys 0m0.022s
