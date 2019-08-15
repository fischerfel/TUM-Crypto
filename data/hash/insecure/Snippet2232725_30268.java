String plainString = "Hash me please";
String md5Hash = "NOTHASHED";
try {
     MessageDigest md5Digest = MessageDigest.getInstance("MD5");
     md5String = new String(md5Digest.digest(plainString.getBytes()));
} catch (NoSuchAlgorithmException nsae) {
     // MD5 is included in all versions of Java, this can never happen
}
