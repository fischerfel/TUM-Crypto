public class CreateChecksum {
    public static void main(String args[]) {
        String test = "Hello world";
        ByteArrayInputStream bis = new ByteArrayInputStream(test.getBytes());
        System.out.println("MD5 checksum for file using Java : "    + checkSum(bis));
        System.out.println("MD5 checksum for file using Java : "    + checkSum(bis));
    }
    public static String checkSum(InputStream fis){
        String checksum = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Using MessageDigest update() method to provide input
            byte[] buffer = new byte[8192];
            int numOfBytesRead;
            while( (numOfBytesRead = fis.read(buffer)) > 0){
                md.update(buffer, 0, numOfBytesRead);
            }
            byte[] hash = md.digest();
            checksum = new BigInteger(1, hash).toString(16); //don't use this, truncates leading zero
        } catch (Exception ex) {                  
        }
       return checksum;
    }
}
