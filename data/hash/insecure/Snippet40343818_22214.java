MessageDigestmdigest = MessageDigest.getInstance("SHA-1");
byte[] digestResult =digest.digest(concatString.getBytes("UTF-8"));
String calculatedDigest = Base64.encode(digestResult);
