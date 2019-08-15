public byte[] digestFile( File f ){
  try {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
    FileInputStream fis = new FileInputStream( f );
    byte[] buffer = new byte[1024];
    int read = -1;
    while ((read = fis.read(buffer)) != -1) {
      messageDigest.digest(buffer, 0, read);
    }
    return messageDigest.digest();
  } catch (VariousExceptions e) {
    //handle
  }
}
