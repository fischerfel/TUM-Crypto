MessageDigest sha1 = MessageDigest.getInstance("SHA1");
byte[] data = text.getBytes("UTF-8");
byte[] hash = sha1.digest(data);
