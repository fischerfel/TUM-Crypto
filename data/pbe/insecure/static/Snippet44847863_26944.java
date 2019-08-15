  // reading the key 
  String fileName = "C://somewhere//aesKey.dat";
  FileInputStream keyFis = new FileInputStream(fileName);
  byte[] encKey = new byte[keyFis.available()];
  keyFis.read(encKey);
  keyFis.close();

  SecretKeyFactory factory = SecretKeyFactory.getInstance(keyFis.toString());
  char[] password = "Pass@word1".toCharArray();
  byte[] salt = "S@1tS@1t".getBytes("UTF-8");

  KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
  SecretKey tmp = factory.generateSecret(spec);
  byte[] encoded = tmp.getEncoded();
  return new SecretKeySpec(encoded, "AES");
