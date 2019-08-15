String id = "test";
byte[] bytesOfMessage = id.getBytes("UTF-8");
System.out.println("bytesOfMessage length: " + bytesOfMessage.length);
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] thedigest = md.digest(bytesOfMessage);
System.out.println("thedigest Hex: " + Hex.encodeHexString(thedigest));
String md5Value = new String(thedigest);
System.out.println("md5Value length: " + md5Value.length());
System.out.println("md5Value bytes length: " + md5Value.getBytes().length);
System.out.println("md5Value Hex: " + Hex.encodeHexString(md5Value.getBytes()));

Output:
bytesOfMessage length: 4
thedigest Hex: 098f6bcd4621d373cade4e832627b4f6
md5Value length: 16
md5Value bytes length: 16
md5Value Hex: 093f6bcd4621d373cade4e832627b4f6
