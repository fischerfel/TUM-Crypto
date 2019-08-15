String yourString = "user@email.com"+"timestamp"; 
byte[] bytesOfMessage = yourString.getBytes("UTF-8");
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] thedigest = md.digest(bytesOfMessage);
