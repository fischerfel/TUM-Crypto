import com.sun.org.apache.xml.internal.security.utils.Base64;
MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
md.update(password.getBytes("UTF-8"));
byte[] passwordDigest = md.digest();
System.out.println(Base64.encode(passwordDigest));
