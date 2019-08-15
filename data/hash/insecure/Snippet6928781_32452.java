byte[] bytesOfMessage = tempStr.getBytes("UTF-8"); // Maybe you're not using a charset here
MessageDigest md5 = MessageDigest.getInstance("MD5");
byte[] theDigest = md5.digest(bytesOfMessage);
