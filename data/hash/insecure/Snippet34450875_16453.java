import java.security.*;
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] thedigest = md.digest(_originebyte);
