String input = "chinese lorem ipsum 槏殟殠巘斖蘱飣偓啅撱簻臗";
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] inputBytes = input.getBytes("UTF-16LE");
md.update(input.getBytes("UTF-16LE"));
byte[] enc = md.digest();
String md5Sum = new sun.misc.BASE64Encoder().encode(enc);
System.out.println(md5Sum);
