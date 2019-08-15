byte[] uploadBytes = upload.getBytes();
MessageDigest md5 = MessageDigest.getInstance("MD5");
byte[] digest = md5.digest(uploadBytes);
String hashString = new BigInteger(1, digest).toString(16);
System.out.println("File hash: " + hashString);
