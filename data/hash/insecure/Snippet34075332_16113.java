  public String createHash(File datafile) throws IOException {
    //SNIP - YOUR CODE BEGINS
    MessageDigest md = MessageDigest.getInstance("SHA1");
    FileInputStream fis = new FileInputStream(datafile);
    byte[] dataBytes = new byte[1024];

    int nread = 0; 

    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    }

    byte[] mdbytes = md.digest();

    //convert the byte to hex format
    StringBuffer sb = new StringBuffer("");
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
  //SNAP - YOUR CODE ENDS
  }
  public void writeFile(File target, String hash) {
     try(FileOutputStream fo = new FileOutputStream(target)) {
       fo.write(hash.getBytes());
     } catch(IOException e) {
       System.err.println("No Hash Written for " + target.getName());
     }
  }
