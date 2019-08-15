MessageDigest md;
md = MessageDigest.getInstance("SHA-1");
md.update(text.getBytes());
return new BigInteger(md.digest());
