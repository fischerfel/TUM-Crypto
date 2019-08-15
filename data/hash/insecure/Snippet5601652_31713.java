byte[] bytesOfMessage = readFile("filepath").getBytes("UTF-8");
MessageDigest md = MessageDigest.getInstance("MD5");
String thedigest = Arrays.toString[md.digest(bytesOfMessage)];
