  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
  SecretKeySpec secret = new SecretKeySpec(key, "AES");
  IvParameterSpec vector = new IvParameterSpec(iv);
  cipher.init(Cipher.DECRYPT_MODE, secret, vector);
