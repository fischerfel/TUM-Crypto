  SecretKeySpec skeySpec = new SecretKeySpec(key, "AES/CBC/PKCS5Padding");
  Cipher cipher = Cipher.getInstance("AES")
  cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
