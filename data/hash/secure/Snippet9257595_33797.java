BASE64Decoder decoder=new BASE64Decoder();
byte[] b=decoder.decodeBuffer(r1);            //r1 is the string containing password

MessageDigest md=MessageDigest.getInstance("SHA-512");
md.update(b);
byte[] plaintext=md.digest();
BASE64Encoder encoder=new BASE64Encoder();
String digest1=encoder.encode(plaintext);   //digest1 contains the msg digest
