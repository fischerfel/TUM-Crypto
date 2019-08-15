MessageDigest md = MessageDigest.getInstance("SHA");    
byte[] after = md.digest(before);    
String securityHash =new sun.misc.BASE64Encoder().encode(after);    
