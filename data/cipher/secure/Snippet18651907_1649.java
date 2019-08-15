  private Path outFile;

  private void decryptFile(FileInputStream fis, byte[] initVector, byte[] aesKey, long used) {
    //Assume used = 0 for this function. 
    byte[] chunks = new byte[2048]; //If this number is greater than or equal to the size of the file then we are good.
    try {
      if (outFile.toFile().exists())
        outFile.toFile().delete();
      outFile.toFile().createNewFile();
      FileOutputStream fos = new FileOutputStream(outFile.toFile());
      OutputStreamWriter out = new OutputStreamWriter(fos);
      IvParameterSpec spec = new IvParameterSpec(Arrays.copyOfRange(initVector, 0, 16));
      SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
      Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
      cipher.init(Cipher.DECRYPT_MODE, key, spec);
      int x;
      while ((x = fis.read(chunks, 0, chunks.length)) != -1) {
        byte[] dec = cipher.doFinal(Arrays.copyOfRange(chunks, 0, x));
        out.append(new String(dec));
      }
      out.close();
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(ExceptionUtils.getStackTrace(e));
    }
  }
