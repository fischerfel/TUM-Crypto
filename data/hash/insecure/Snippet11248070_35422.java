BASE64Encoder encoder = new BASE64Encoder();
MessageDigest md = MessageDigest.getInstance("MD5");
md.update(someString.getBytes());
byte[] bMac = md.digest();
String anotherString = encoder.encodeBuffer(bMac);
