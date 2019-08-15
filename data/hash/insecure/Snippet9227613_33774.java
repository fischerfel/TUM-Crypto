import java.security.*;


byte[] bom= str.getBytes("UTF-8");

MessageDigest md = MessageDigest.getInstance("MD5");
byte[] hash = md.digest(bom);
