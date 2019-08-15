String s = "string";
MessageDigest m = MessageDigest.getInstance("MD5");
BigInteger i = new BigInteger(1, m.digest(s.getBytes()));
return i.mod(BigInteger.valueOf(100));
