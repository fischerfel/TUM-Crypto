import java.security.*;

byte[] password;
MessageDigest messageDigest = MessageDigest.getInstance("MD5");
messageDigest.update(password, 0, password.length);
byte[] passwordHashed = messageDigest.digest();
