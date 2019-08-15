public static String SHA2(String text) 
    throws NoSuchAlgorithmException, UnsupportedEncodingException  { 

  MessageDigest mesd = MessageDigest.getInstance("SHA-256");
  byte[] bytes = text.getBytes("iso-8859-1");
  mesd.update(bytes, 0, bytes.length);
  byte[] sha2hash = mesd.digest();
  return convertToHex(sha2hash);
} 
