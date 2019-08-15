MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
String canonDigestValue = Base64.encodeBytes(sha1.digest(canonTimestamp));
