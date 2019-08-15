byte[] bytesOfMessage = yourString.getBytes("UTF-8"); // pass the right encoding
MessageDigest md = MessageDigest.getInstance("MD5");  // specify the algorithm
byte[] thedigest = md.digest(bytesOfMessage);         // here's the hash
