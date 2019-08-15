MessageDigest md5Hash = MessageDigest.getInstance("MD5");
mDigest = md5Hash.digest(password.getBytes());
String hashedMessage = new String(Hex.encodeHex(mDigest));
