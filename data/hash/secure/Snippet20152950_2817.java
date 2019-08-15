import java.security.MessageDigest

...

String password = "<password to be encrypted>";
MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(password.getBytes("UTF-8"));
