import java.security.*;

byte[] bytesOfMessage = key.getBytes("UTF-8");
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] b = md.digest(bytesOfMessage); //Returns the SHA 256 hash and converts it into byte
// Continue with your code
Key secretKey = new SecretKeySpec(b , "AES");
...
