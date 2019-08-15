import java.math.BigInteger;
import java.security.MessageDigest;

class Test {
  public static void main(String []args) throws Exception {
    String SECRET = "secret";
    String STRING = "string";
    MessageDigest digest = MessageDigest.getInstance("SHA-512");
    digest.update(SECRET.getBytes("UTF-8"));
    byte[] d = digest.digest(STRING.getBytes("UTF-8"));
    System.out.println(new BigInteger(1, d).toString(16));
  }
}
