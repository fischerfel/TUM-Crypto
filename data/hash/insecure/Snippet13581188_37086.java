import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//Some more imports

MessageDigest md = MessageDigest.getInstance("SHA-1");
md.update(data);
byte[] hash = md.digest());
