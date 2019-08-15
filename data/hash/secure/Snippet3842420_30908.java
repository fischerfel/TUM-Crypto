 private static String generateHashFromFile(String filePath) {
  try {
   final int BUFSZ = 32768;
   MessageDigest sha = MessageDigest.getInstance("SHA-256");
   FileInputStream in = new FileInputStream(filePath);
   BufferedInputStream is = new BufferedInputStream(in, BUFSZ);
   byte[] buffer = new byte[BUFSZ];
   int num = -1;
   while((num = is.read(buffer)) != -1) {
    sha.update(buffer, 0, num);
   }
   is.close();
   byte[] hash = sha.digest();
   return byteArrayToHex(hash);
  } catch (NoSuchAlgorithmException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (FileNotFoundException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  return null;
 }

 private static String byteArrayToHex(byte[] barray)
 {
     char[] c = new char[barray.length * 2];
     byte b;
     for (int i = 0; i < barray.length; ++i)
     {
         b = ((byte)(barray[i] >> 4));
         c[i * 2] = (char)(b > 9 ? b + 0x37 : b + 0x30);
         b = ((byte)(barray[i] & 0xF));
         c[i * 2 + 1] = (char)(b > 9 ? b + 0x37 : b + 0x30);
     }
     return new String(c);
 }
