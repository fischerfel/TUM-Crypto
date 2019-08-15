String s = "string";
MessageDigest m = MessageDigest.getInstance("MD5");
m.update(s.getBytes(), 0, s.length());
BigInteger i = new BigInteger(1, m.digest());
return i.mod(BigInteger.valueOf(100));
