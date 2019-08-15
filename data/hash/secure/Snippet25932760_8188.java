public static String SHA256(String path) throws NoSuchAlgorithmException, IOException {

  MessageDigest md = MessageDigest.getInstance("SHA-256");
  FileInputStream fis = null;

  try {

    fis = new FileInputStream(path);

    byte[] dataBytes = new byte[1024];

    int nread = 0;

    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    }
  }

  //
  // here you can also log something about exception

  catch (IOException e) {
    throw e;
  }

  //
  // the stream will be closed even in case of unexpected exceptions 

  finally {
    if (fis != null) {
      try {
        fis.close();
      } catch (IOException e) {
        // ignore, nothing to do anymore
      }
    }
  }

  byte[] mdbytes = md.digest();

  StringBuilder sb = new StringBuilder();
  for (int i = 0; i < mdbytes.length; i++) {
    sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
  }

  return sb.toString();
}
