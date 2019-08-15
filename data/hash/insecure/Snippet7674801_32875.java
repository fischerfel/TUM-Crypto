import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
...
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] digestBytes = md.digest(string.getBytes());
