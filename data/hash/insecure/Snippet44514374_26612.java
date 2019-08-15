FileInputStream fis = new FileInputStream(path);
MessageDigest digest = MessageDigest.getInstance("MD5");
byte[] buffer = new byte[1024];
int len;
while ((len = fis.read(buffer)) != -1) {
  digest.update(buffer, 0, len);
}
BigInteger bigInt = new BigInteger(1, digest.digest());
return  bigInt.toString(16);
