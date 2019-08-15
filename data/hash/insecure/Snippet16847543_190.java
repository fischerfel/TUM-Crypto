byte[] pwBytes = new String("password".toUpperCase().getBytes(), "UTF-16").getBytes();

MessageDigest md = null;
md = MessageDigest.getInstance("SHA-1");
byte[] sha1pw = md.digest(pwBytes);

final BASE64Encoder encoder = new BASE64Encoder();
String encodedPw = encoder.encode(sha1pw);
