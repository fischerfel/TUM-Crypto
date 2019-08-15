FileInputStream fis = new FileInputStream(path);
MessageDigest digest = MessageDigest.getInstance("MD5");
byte[] buffer = new byte[1024];
while ((fis.read(buffer)) != -1) {
  digest.update(buffer);
}
BigInteger bigInt = new BigInteger(1, digest.digest());
return  bigInt.toString(16);
