MessageDigest sha = MessageDigest.getInstance("SHA");
MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
String shaString = new BASE64Encoder().encode(sha.digest("foobarbaz".getBytes()));
String sha1String = new BASE64Encoder().encode(sha1.digest("foobarbaz".getBytes()));
System.out.println(shaString);
System.out.println(sha1String);
