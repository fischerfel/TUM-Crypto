 byte[] hash = provider.digest(password.getBytes(StandardCharsets.UTF_8));
 MessageDigest m = MessageDigest.getInstance("MD5");     
 String s = new BigInteger(1, hash).toString(16).toLowerCase();
