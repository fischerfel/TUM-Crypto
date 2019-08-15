try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      File f = new File(fileName);
      InputStream is = new FileInputStream(f);              
      byte[] buffer = new byte[8192];
      int read = 0;
      while( (read = is.read(buffer)) > 0) {
          digest.update(buffer, 0, read);
      } 
      is.close();
      byte[] md5sum = digest.digest();
      BigInteger bigInt = new BigInteger(1, md5sum);
      output = bigInt.toString(16);
      System.out.println("MD5: " + output);
  } catch(IOException e) {
      throw new RuntimeException("Unable to process file for MD5", e);
  } catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
  }
