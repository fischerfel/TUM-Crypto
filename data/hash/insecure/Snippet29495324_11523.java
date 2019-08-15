import java.security.MessageDigest;    
import sun.misc.BASE64Encoder;

public class MD5Sample {
  /**
   * Cipher for encode
   */
  private final MessageDigest md;

  public MD5Sample() throws SecurityException {
    try {
      md = MessageDigest.getInstance("MD5", "SUN");
    }catch(Exception se) {
      throw new SecurityException("In MD5 constructor " + se);
    }
  }

  public String encode(String in) throws Exception {
    if (in == null) {
      return null;
    }
    try {
      byte[] raw = null;
      byte[] stringBytes = null;
      stringBytes = in.getBytes("UTF8");
      synchronized(md) {
        raw = md.digest(stringBytes);
      }
      BASE64Encoder encoder = new BASE64Encoder();
      return encoder.encode(raw);
    } catch (Exception se) {
      throw new Exception("Exception while encoding " + se);
    }

  }

  public String decode(String in) {
    throw new RuntimeException("NOT SUPPORTED");
  }

  /**
   * Test harness
   * @param args
   */
  public static void main(String[] args) {
     String clearText = "apple";
    try {
      MD5Sample app = new MD5Sample();
      String encryptedHash = app.encode(clearText);
      System.out.println(encryptedHash);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
