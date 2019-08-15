private IvParameterSpec getIV(String uid, String pin) throws Exception {
  String ivValue = new StringBuilder(uid).reverse().toString() + new StringBuilder(pin).reverse();
  byte[] key = ivValue.getBytes("UTF-8");
  MessageDigest sha = MessageDigest.getInstance("SHA-256");
  key = sha.digest(key);
  key = Arrays.copyOf(key, 16);
  IvParameterSpec iv = new IvParameterSpec(key);
  return iv;
}
