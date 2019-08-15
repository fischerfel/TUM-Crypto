 {
  setupKeys(IV, KEY,keys);

  // setup the cipher with the keys
  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
  SecretKeySpec key = new SecretKeySpec(KEY, "AES/CBC/PKCS7Padding");
  IvParameterSpec ips = new IvParameterSpec(IV);
  cipher.init(Cipher.DECRYPT_MODE, key,ips);

  // decrypt a file
  byte[] buffer = new byte[1024];
  InputStream iFile = new FileInputStream(fileName);
  OutputStream oFile = new FileOutputStream(fileOut);

  iFile = new CipherInputStream(iFile, cipher);

  int r = 0;
  while ((r = iFile.read(buffer, 0, 1024)) > 0) {
   oFile.write(buffer, 0, r);
  }
  oFile.close();
  iFile.close();
 }
 private void setupKeys(byte[] IV,byte[] KEY,String keys)
 {
     String[] keyparts = keys.split(",");
     for (int i = 0; i < 16; i++)
     {
         Long l = Long.parseLong(keyparts[i]);
         IV[i] = (byte) (l.byteValue() );
         if (siv == null) siv = l.toString();
         else    siv = siv + l.toString();
     }
     for (int i = 16; i < 47; i++)
     {
         Long l = Long.parseLong(keyparts[i]);
         KEY[i - 16] = (byte) (l.byteValue() & 0xff);
         if (skey == null ) skey = l.toString();
         else            skey = skey + l.toString();
     }

 }
