  FileInputStream fis = new FileInputStream(file);
  FileOutputStream fos = new FileOutputStream(tmp_file);
  String seed = "password";
  byte[] rawKey = Utils.getRawKey(seed.getBytes());
  SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
  Cipher cipher = Cipher.getInstance("AES");
  cipher.init(Cipher.DECRYPT_MODE, skeySpec);
  int b;
  byte[] data = new byte[4096];
  while((b = fis.read(data)) != -1) {
       fos.write(cipher.doFinal(data), 0, b);
  }
 fos.flush();
 fos.close();
 fis.close();
