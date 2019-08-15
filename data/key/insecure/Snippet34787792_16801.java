  String token = "1345BCHCNB";
  Cipher ecipher = Cipher.getInstance("AES");
  String mykey = "1234567891234567";
  SecretKey key = new SecretKeySpec(mykey.getBytes(), "AES");
  ecipher.init(Cipher.ENCRYPT_MODE, key);
  byte[] utf8 = token.getBytes("UTF-8");
  byte[] enc = ecipher.doFinal(utf8);
  String enctoken = Base64.encodeBase64(enc).toString());
