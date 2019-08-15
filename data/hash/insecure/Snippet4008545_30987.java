private static void testGet() throws IOException {

    // Create a URL
    URL test = new URL("http://localhost:8080/TestWebProject/TestServlet");

    // Open a connection to the URL
    HttpURLConnection conn = (HttpURLConnection) test.openConnection();

    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch(NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    // Digest password using the MD5 algorithm
    String password = "1234";
    md5.update(password.getBytes());
    String digestedPass = digest2HexString(md5.digest());

    // Set header "Authorization"
    String credentials = "testuser:" + digestedPass;
    conn.setRequestProperty("Authorization", "Digest " + credentials);

    // Print status code and message
    System.out.println("Test HTTP GET method:");
    System.out.println("Status code: " + conn.getResponseCode());
    System.out.println("Message: " + conn.getResponseMessage());
    System.out.println();

}

private static String digest2HexString(byte[] digest)
{
   String digestString="";
   int low, hi ;

   for(int i=0; i < digest.length; i++)
   {
      low =  ( digest[i] & 0x0f ) ;
      hi  =  ( (digest[i] & 0xf0)>>4 ) ;
      digestString += Integer.toHexString(hi);
      digestString += Integer.toHexString(low);
   }
   return digestString ;
}
