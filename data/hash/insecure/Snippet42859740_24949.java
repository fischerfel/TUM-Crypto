import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JavaApplication1 {

    public static void main(String[] args) throws NoSuchAlgorithmException  {
      String b = "0111001101101000011001";

      byte[] bval = new BigInteger(b, 2).toByteArray();

      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] hash = md.digest(bval);

      for (byte b1 : hash) {
        System.out.print(String.format("%02X", b1));
      }
    }
}
