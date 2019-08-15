/** Returns a MD5 checksum from a file
 * 
 * @param filename file name to write
 * @return String
 * @throws Exception
 */
private static String createChecksumForFile(String filename) throws Exception
    {
  InputStream fis =  new FileInputStream(filename);

  byte[] buffer = new byte[1024];
  MessageDigest complete = MessageDigest.getInstance("MD5");
  int numRead;
  do {
    numRead = fis.read(buffer);
    if (numRead > 0) {
      complete.update(buffer, 0, numRead);
    }
  } while (numRead != -1);

  fis.close();

  byte[] b = complete.digest();
  String result = "";
  for (byte aB : b) {
     result +=
             Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
  }
  return result;
} 
