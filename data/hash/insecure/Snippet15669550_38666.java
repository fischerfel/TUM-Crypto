String ss = "9a";
ByteBuffer bb = ByteBuffer.allocate(8);  
byte[] ba = bb.putLong(Long.decode("0x"+ss).longValue()).array();
MessageDigest md = MessageDigest.getInstance("SHA-1");
String results = encodeHex(md.digest(ba));
System.out.println("sha:"+results);
