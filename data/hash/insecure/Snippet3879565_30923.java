  if(toSecurify == null) {
   throw new Exception("toSecurifyString must not be null");
  }
  try {
   MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
   byte[] sha1HashBytes = new byte[40];
   messageDigest.update(toSecurify.getBytes(), 0, toSecurify.length());
   sha1HashBytes = messageDigest.digest();
   return new String(sha1HashBytes, "UTF-8");
  } catch(NoSuchAlgorithmException nsae) {
   throw new Exception("Hash algorithm not supported.");
  } catch(UnsupportedEncodingException uee) {
   throw new Exception("Encoding not supported.");
  }
