  SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");// here i need to replace
  SecretKeyFactory factory = SecretKeyFactory.getInstance(keyFis.toString());
  char[] password = "Pass@word1".toCharArray();
  byte[] salt = "S@1tS@1t".getBytes("UTF-8");

  KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
  SecretKey tmp = factory.generateSecret(spec);
  byte[] encoded = tmp.getEncoded();
  return new SecretKeySpec(encoded, "AES");
