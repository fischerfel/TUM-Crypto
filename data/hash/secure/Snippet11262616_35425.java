MessageDigest md   = MessageDigest.getInstance("SHA-256"); //make sure it exists, there are other algorithms, but I prefer SHA for simple and relatively quick hashing
String strToEncode = "Hello world";
md.update(strToEncode.getBytes("UTF-8")); //I'd rather specify the encoding. It's platform dependent otherwise. 
byte[] digestBuff = md.digest();
