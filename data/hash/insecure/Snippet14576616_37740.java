import java.security.*;
.....
string hash = unique_salt_key.concat(user_id).concat(email);
byte[] hashBytes = hash.getBytes("UTF-8");
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] emailCrypt = md.digest(hashBytes);
string emailToken = new String(emailCrypt)
