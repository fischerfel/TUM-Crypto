import java.security.*;
import java.math.*;    

String s = "string";
MessageDigest m = MessageDigest.getInstance("MD5");
m.update(s.getBytes(), 0, s.length());
BigInteger i = BigInteger(1,m.digest());
return i % 100;
