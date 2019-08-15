MessageDigest md = MessageDigest.getInstance("SHA-1");
byte[] d = md.digest("John Smith".getBytes());
String str = javax.xml.bind.DatatypeConverter.printBase64Binary(d);
System.out.println(str);
