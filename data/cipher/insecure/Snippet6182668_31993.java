try {
    FileInputStream inFile = new FileInputStream(f.getAbsolutePath());
    FileOutputStream outFile = new FileOutputStream(f.getAbsoluteFile() + ".des");

    PBEKeySpec keySpec = new PBEKeySpec(text.toCharArray());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey passwordKey = keyFactory.generateSecret(keySpec);
    byte[] salt = new byte[8];
    Random rnd = new Random();
    rnd.nextBytes(salt);
    int iterations = 100;

    PBEParameterSpec paramaterSpec = new PBEParameterSpec(salt, iterations);

    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
    cipher.init(Cipher.ENCRYPT_MODE, passwordKey, paramaterSpec);

    outFile.write(salt);



    byte[] input = new byte[inFile.available()];

    int bytesRead;
     while ((bytesRead = inFile.read(input)) != -1)
                {
                  byte[] output = cipher.update(input, 0, bytesRead);
                   if (output != null) outFile.write(output);
               }

               byte[] output = cipher.doFinal();
               if (output != null) outFile.write(output);
               f.delete();
              inFile.close();
               outFile.flush();
               outFile.close();
