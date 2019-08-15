byte[] bytes = stringToConvert.getBytes("UTF-8");
MessageDigest m = MessageDigest.getInstance("MD5");
hashedBytes = m.digest(bytes);
