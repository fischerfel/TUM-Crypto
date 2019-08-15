byte[] b = r1.getBytes("UTF-8");
MessageDigest md=MessageDigest.getInstance("SHA-512");
md.update(b);
byte[] plaintext=md.digest();
BASE64Encoder encoder=new BASE64Encoder();
String digest1=encoder.encode(plaintext);   //digest1 contains the msg digest
