MessageDigest digest = MessageDigest.getInstance("MD5");
digest.update("CUSTOMSTRING"+pass.getBytes(), 0, 12+pass.length());
String md5 = new BigInteger(1, digest.digest()).toString(16);
