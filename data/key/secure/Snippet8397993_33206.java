private Key buildKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
  MessageDigest digester = MessageDigest.getInstance("SHA-256");
  digester.update(password.getBytes("UTF-8"));
  byte[] key = digester.digest();
  SecretKeySpec spec = new SecretKeySpec(key, "AES");
  return spec;
}
