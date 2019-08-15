MessageDigest m=MessageDigest.getInstance("MD5");
m.update(message.getBytes(), 0, message.length());
BigInteger bi = new BigInteger(1,m.digest());
