MessageDigest digest=MessageDigest.getInstance("MD5");
String test="test";
digest.update(test.getBytes());
byte hash[]=digest.digest();
