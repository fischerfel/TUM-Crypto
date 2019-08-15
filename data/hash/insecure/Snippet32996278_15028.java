import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainClass {

private static final String ALGORITHM = "AES";
private static final String keyValue = "thisisasecretkey";
final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

public static void main(String[] args) throws Exception {
    System.out.println(encrypt("hello world"));
}

public static String encrypt(String valueToEnc) throws Exception {
  Key key = generateKey();
  Cipher cipher = Cipher.getInstance(ALGORITHM);
  cipher.init(Cipher.ENCRYPT_MODE, key);
  byte[] encValue = cipher.doFinal(valueToEnc.getBytes());
  System.out.println(bytesToHex(encValue));
  return new String(encValue);
}

private static Key generateKey() throws Exception {
  byte[] key2 = keyValue.getBytes("UTF-8");
  MessageDigest sha = MessageDigest.getInstance("SHA-1");
  key2 = sha.digest(key2);
  key2 = Arrays.copyOf(key2, 16);

  Key key = new SecretKeySpec(key2, ALGORITHM);
  return key;
}

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
