MessageDigest md = MessageDigest.getInstance("MD5");
md.update(password.getBytes());
String hashedPass = new BigInteger(1, md.digest()).toString(16);
