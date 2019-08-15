MessageDigest md5 = MessageDigest.getInstance("MD5");
byte[] bytes = ...;
for (String toHash: stringsToHash) {
  md5.update(toHash.getBytes("UTF-8"));
}
md5.digest(bytes);
