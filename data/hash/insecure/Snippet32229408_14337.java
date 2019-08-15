MessageDigest md = MessageDigest.getInstance("SHA-1");
md.update(clearPassword.getBytes("UTF-8"));
return new BigInteger(1 ,md.digest()).toString(16));
