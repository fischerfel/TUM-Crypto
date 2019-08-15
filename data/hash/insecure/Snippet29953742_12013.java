  MessageDigest md5Digest = MessageDigest.getInstance("MD5");
  String md5String = DatatypeConverter.printHexBinary(md5Digest.digest((emailAddress + ":" + expiryTime + ":" + password + ":" + key).getBytes()));
  String token = emailAddress + ":" + expiryTime + ":" + md5String;
  Encoder encoder = Base64.getEncoder();
  String encodedToken = encoder.encodeToString(token.getBytes());
