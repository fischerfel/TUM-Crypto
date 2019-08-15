public String hashSha1(byte[] data) throws NoSuchAlgorithmException, 
    UnsupportedEncodingException { 
  MessageDigest md = MessageDigest.getInstance("SHA-1");
  md.update(data, 0, data.length);
  return convertToHex(md.digest());
}
