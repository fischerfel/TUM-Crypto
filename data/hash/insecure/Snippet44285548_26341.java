 public static String checkSum(String path){
    String checksum = null;
    try {
        FileInputStream fis = new FileInputStream(path);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int numOfBytesRead;
        while( (numOfBytesRead = fis.read(buffer)) &gt; 0){
            md.update(buffer, 0, numOfBytesRead);
        }
        byte[] hash = md.digest();
        checksum = new BigInteger(1, hash).toString(16);
    } catch (IOException ex) {
    } catch (NoSuchAlgorithmException ex) {
    }

   return checksum;
}
