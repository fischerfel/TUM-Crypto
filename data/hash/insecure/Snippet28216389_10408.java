MessageDigest messageDigest = MessageDigest.getInstance("MD5");
byte[] digest = messageDigest.digest("NSI#1234@".getBytes("UTF-8"));
String hash = new BigInteger(1, digest).toString();
System.out.println(hash);
