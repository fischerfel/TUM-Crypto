  byte [] key = "AAAAA".getBytes("ASCII");

  String clearText = "66";


  Cipher rc4 = Cipher.getInstance("RC4");
  SecretKeySpec rc4Key = new SecretKeySpec(key, "RC4");
  rc4.init(Cipher.ENCRYPT_MODE, rc4Key);
  byte [] cipherText = rc4.update(clearText.getBytes("ASCII"));
