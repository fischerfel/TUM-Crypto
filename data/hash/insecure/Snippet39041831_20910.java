import java.util.Date;
import java.security.MessageDigest;
import java.math.BigInteger;

long str = System.currentTimeMillis() / 1000L;
String Timestamp = Long.toString(str);
//System.out.println(Timestamp);




String seconds2 = str + "8fo9aw8uefawejfoi";
MessageDigest md5 = MessageDigest.getInstance("MD5");
md5.update(seconds2.getBytes());
BigInteger hash = new BigInteger(1, md5.digest());
String Token = hash.toString(16);
//System.out.println(Token);

vars.put("Timestamp", Timestamp);  
vars.put("Token", Token)
