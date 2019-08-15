 byte[] key = stringToHash.getBytes();
 MessageDigest md = MessageDigest.getInstance("SHA-1");
 hash = md.digest(key);
