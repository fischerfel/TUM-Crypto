MessageDigest md = MessageDigest.getInstance("MD5");
md.update(str.getBytes("UTF-8"));
return new BigInteger(1, md.digest()).intValue();
