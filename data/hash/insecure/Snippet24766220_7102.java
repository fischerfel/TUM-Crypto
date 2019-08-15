import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {
  public static void main(String[] args) throws Exception {
    MessageDigest m = MessageDigest.getInstance("MD5");

    m.update("\u00db".getBytes());
    System.out.println(bytesToHex(m.digest()));

    m.update("Û".getBytes());
    System.out.println(bytesToHex(m.digest()));
  }

  final protected static char[] hexArray = "0123456789abcdef".toCharArray();
  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }
}
