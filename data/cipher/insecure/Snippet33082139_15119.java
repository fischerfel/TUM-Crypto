 // encripted file stored in android device for decrypt
 String uri= Environment.getExternalStorageDirectory().toString();
 uri=uri+"/encry_file.mp4";
 File file = new File(uri.toString());
 FileInputStream fis = new FileInputStream(file);
 spec =getIV();

 FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/decrypted.mp4");
 SecretKeySpec sks = new SecretKeySpec("asdfghjklzxccvbn".getBytes(),
          "AES");
 Cipher cipher = Cipher.getInstance("AES");
 cipher.init(Cipher.DECRYPT_MODE, sks, spec);
 CipherInputStream cis = new CipherInputStream(fis, cipher);

 int b;
 byte[] d = new byte[8192];
 while ((b = cis.read(d)) != -1) {
    fos.write(d, 0, b);
 }
 fos.flush();
 fos.close();
 cis.close();
