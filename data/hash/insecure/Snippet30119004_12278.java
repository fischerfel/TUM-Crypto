MessageDigest md5 = MessageDigest.getInstance("MD5");
md5.update(StandardCharsets.UTF_8.encode(string));
return String.format("%032x", new BigInteger(1, md5.digest()));
