MessageDigest md;
md = MessageDigest.getInstance("SHA-1");
md.update(text.getBytes());
return new BigInteger(1, md.digest()); // use this 1 to tell it is positive.
