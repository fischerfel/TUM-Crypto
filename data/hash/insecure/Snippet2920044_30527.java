MessageDigest m = MessageDigest.getInstance("MD5");
m.update(bytes,0,bytes.length);

String hashcode = new BigInteger(1,m.digest()).toString(16);
return hashcode;
