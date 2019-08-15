importClass(java.security.MessageDigest);

var md = MessageDigest.getInstance("SHA-1");
var sha1 = md.digest((new java.lang.String("abc")).getBytes());
