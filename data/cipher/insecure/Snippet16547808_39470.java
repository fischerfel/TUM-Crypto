 inStream = new BufferedInputStream(conn.getInputStream());
 outFile = new File(path + fileName);
 outStream = new BufferedOutputStream(new FileOutputStream(outFile), 4096);
 byte[] data = new byte[4096];
 String seed = "password";
 byte[] rawKey = Utils.getRawKey(seed.getBytes());
 SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
 Cipher cipher = Cipher.getInstance("AES");
 cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
 int bytesRead = 0;
 while((bytesRead = inStream.read(data, 0, data.length)) >= 0)
 {
      outStream.write(cipher.doFinal(data),0, bytesRead);
 }
 outStream.flush();
 outStream.close();  
 inStream.close();
