byte[] bytesOfMessage = password.getBytes("UTF-8");
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] thedigest = md.digest(bytesOfMessage);
