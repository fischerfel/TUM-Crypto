private static void receive(InputStream ip, File fname,
        PrintWriter output2) throws Throwable    {


    byte[] ivBytes = "1234567812345678".getBytes();

    Cipher dcipher ;
    DESKeySpec desKeySpec = new DESKeySpec(ivBytes);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey sKey = keyFactory.generateSecret(desKeySpec);

    dcipher = Cipher.getInstance("DES");
    dcipher.init(Cipher.DECRYPT_MODE, sKey);

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    byte[] buffer = new byte[1024]; 
    int length;

    while ((length = ip.read(buffer)) != -1)
     { 
       out.write(buffer, 0, length); 
      }

    byte[] result = out.toByteArray();

    byte[] outputBytes = dcipher.doFinal(result);

    FileOutputStream outputStream = new FileOutputStream(fname);
    outputStream.write(outputBytes);
    outputStream.close();

    System.out.println("File received");

 }
