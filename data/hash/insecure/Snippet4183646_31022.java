import java.security.*;
....
String s = "getTokenapi_keybf8ddfs845jhre980543jhsjfro93fd8capi_ver1tokeniud9ER£jdfff";
byte[] bytesOfMessage = s.getBytes("UTF-8");
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] thedigest = md.digest(bytesOfMessage);    

System.out.println("String2: " + thedigest);        
System.out.println("String3: " + new String(thedigest));
