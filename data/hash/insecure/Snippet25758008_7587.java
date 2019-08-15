MessageDigest digest = MessageDigest.getInstance("SHA-1");
digest.update(message.getBytes("utf8"));
byte[] digestBytes = digest.digest();
String digestStr = javax.xml.bind.DatatypeConverter.printHexBinary(digestBytes);
