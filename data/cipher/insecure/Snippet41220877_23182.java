FileInputStream fis = new FileInputStream(file);
  File mediaStorage= new File(
           Environment.getExternalStorageDirectory(),
           "/Sams");


   if (!mediaStorage.exists()) {
       if (!mediaStorage.mkdirs()) {
           Log.d("App", "failed to create directory");
       }
   }
   FileOutputStream fos = new FileOutputStream(mediaStorage+"x.mp4");


    SecretKeySpec sks = new SecretKeySpec(pass.getBytes(), "AES");

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);

    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    int b;
    byte[] d = new byte[1024];
    while((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }

    cos.flush();
    cos.close();
    fis.close();


}
