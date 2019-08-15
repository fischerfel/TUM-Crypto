import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoopTest {

  public static void main(String[] args) {
    MessageDigest d;
    try {
      d = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      System.out.println("NoSuchAlgorithmException");
      return;
    }

    System.out.println("Entry:");
    byte[] r = new byte[] {1};
    System.out.println(toHex(r));

    for(int i = 0; i < 2; i++) {
      System.out.printf("Loop %d\n", i);
      d.update(r);
      r = d.digest();
      System.out.println(toHex(r));
    }
  }

  private static String toHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length);
    for (byte b: bytes) {
       sb.append(String.format("0x%02X ", b));
    }
    return sb.toString();
  }
}
