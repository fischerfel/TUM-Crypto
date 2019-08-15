import java.security.MessageDigest;
import java.math.BigInteger;
import java.lang.String

public class SecurityUtil {

  public static String stringToMD5(String string) throws NoSuchAlgorithmException {

    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    messageDigest.update(string.getBytes(Charset.forName("UTF-8")),0,string.length());
    return new BigInteger(1,messageDigest.digest()).toString(16);
  }
}
