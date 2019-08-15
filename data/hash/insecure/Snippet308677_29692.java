try
{
  String digestInput = "queen";

  MessageDigest messageDigest = MessageDigest.getInstance("MD5");
  messageDigest.update(digestInput.getBytes());

  BASE64Encoder base64Encoder = new BASE64Encoder();
  String digestString = base64Encoder.encode(messageDigest.digest());

  // digestString now contains the md5 hashed password
}
catch (Exception e)
{
  // do some type of logging here
}
