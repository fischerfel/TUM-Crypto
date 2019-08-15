String s = "KO00001"+"25/10/2012";
byte[] buffer = s.getBytes();
MessageDigest md5 = MessageDigest.getInstance("MD5");
md5.update(buffer);
String str = new BigInteger(1,md5.digest()).toString(16).toUpperCase();
System.out.println(str+"    length:"+str.length());
