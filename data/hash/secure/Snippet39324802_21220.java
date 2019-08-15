byte[] bytesOfMessage = yourString.getBytes("UTF-8");

MessageDigest md = MessageDigest.getInstance(MD5_ALGORITH);
byte[] thedigest = md.digest(bytesOfMessage);
