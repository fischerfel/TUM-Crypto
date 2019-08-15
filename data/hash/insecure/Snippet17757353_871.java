private static String readFileToString(String filePath)
        throws java.io.IOException{

            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(
                    new FileReader(filePath));
            char[] buf = new char[1024];

            int numRead=0;
            while((numRead=reader.read(buf)) != -1){
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }

            reader.close();
            System.out.println(fileData.toString());
            return fileData.toString();
        }

public static String getMD5EncryptedString(String encTarget){
  MessageDigest mdEnc = null;
  try {
      mdEnc = MessageDigest.getInstance("MD5");
  } catch (NoSuchAlgorithmException e) {
      System.out.println("Exception while encrypting to md5");
      e.printStackTrace();
  } // Encryption algorithm
  mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
  String md5 = new BigInteger(1, mdEnc.digest()).toString(16) ;
  return md5;
}
