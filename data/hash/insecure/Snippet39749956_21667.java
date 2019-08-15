byte[] result = hmac.doFinal();
MessageDigest md = MessageDigest.getInstance("SHA-1");
String sha1Hash = byteArrayToHex(md.digest(result));
