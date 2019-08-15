class JceSha1Test {

  private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

  public static void main(String[] a) {
      try {
        String strHash = "JAO21V279RSNHYGX23L0";        
        strHash = encryptPassword(strHash);        
        System.out.println("strHash: " + strHash);
      } catch (Exception e) {
         System.out.println("Exception: "+e);
      }
   }
  public static String encryptPassword(String password) {
    String returnValue = null;
    byte[] buf = password.getBytes(UTF8_CHARSET);

    ByteBuffer bb = ByteBuffer.wrap( buf );
    MessageDigest algorithm=null;
    try {
      algorithm = MessageDigest.getInstance("SHA-1");

    } catch (NoSuchAlgorithmException e) {
      //sClassLog.logException(e);
    }
    algorithm.reset();
    algorithm.update(buf);
    byte[] digest = algorithm.digest();
    returnValue = "";

    for (int byteIdx = 0; byteIdx < digest.length; byteIdx++) {
      //System.out.println( Integer.toHexString(digest[byteIdx]) );
      //returnValue += Integer.toHexString(digest[byteIdx] + 256  & 0xFF);
      //returnValue += Integer.toHexString((digest[byteIdx] + 256   & 0xFF) + 0x100 );
      //returnValue += Integer.toHexString( ( digest[byteIdx] & 255 ) );
      //returnValue += Integer.toHexString( ( 0xFF & digest[byteIdx] ) );
      //returnValue += Integer.toHexString( ( digest[byteIdx] & 0xFF ) );      
      returnValue += Integer.toString( ( digest[byteIdx] & 0xFF ) + 0x100, 16).substring( 1 );      
      //returnValue += Integer.toHexString( ( digest[byteIdx] + 256 ) ); // Orig
    }
    return returnValue;    
  }
//A4-B7-83-01-00-59-25-A8-B5-7C-F7-16-E6-69-CF-14-A2-2E-22-09
//-1843714884904279543
}
