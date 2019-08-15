    public void main(String[] args) throws Exception
      {

         // File to decrypt.
      filename = "/file.enc";

      String password = "codecodecode";

      inFile = new FileInputStream(new File(Environment.getExternalStorageDirectory()+ filename));
      outFile = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+ filename + ".txt"));

      PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
      SecretKeyFactory keyFactory =
          SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey passwordKey = keyFactory.generateSecret(keySpec);

      // Read in the previouly stored salt and set the iteration count.

      byte[] salt = new byte[8];
      inFile.read(salt);
      int iterations = 100;

      PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

      // Create the cipher and initialize it for decryption.

      Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
      cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);


      byte[] input = new byte[64];
      int bytesRead;
      while ((bytesRead = inFile.read(input)) != -1)
      {
         byte[] output = cipher.update(input, 0, bytesRead);
         if (output != null)
            outFile.write(output);
      }

      byte[] output = cipher.doFinal();
      if (output != null)
         outFile.write(output);

      inFile.close();
      outFile.flush();
      outFile.close();
  }
