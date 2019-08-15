context.setAttributeValue("userPassword", digestMd5("newPassword));

private String digestMd5(final String password) {
  String base64;
  try {
     MessageDigest digest = MessageDigest.getInstance("MD5");
     digest.update(password.getBytes());
     base64 = new BASE64Encoder().encode(digest.digest());
  }
  catch (NoSuchAlgorithmException e) {
     throw new RuntimeException(e);
  }
  return "{MD5}" + base64;
}
