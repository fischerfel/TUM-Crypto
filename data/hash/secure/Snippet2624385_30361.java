import java.security.MessageDigest;

MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
messageDigest.update(stringToEncrypt.getBytes());
String encryptedString = new String(messageDigest.digest());
