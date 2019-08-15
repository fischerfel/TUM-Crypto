MessageDigest md5 = MessageDigest.getInstance("MD5");
byte[] digest = md5.digest("Wallace".getBytes("UTF-8"));
long result = ByteBuffer.wrap(digest).getLong();
