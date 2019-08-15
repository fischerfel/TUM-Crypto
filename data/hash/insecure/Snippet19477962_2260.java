 import java.math.BigInteger;
 import java.security.MessageDigest;
 import java.security.NoSuchAlgorithmException;

/**
* MD5 encryption
*
* @author Hongten
*
*/
public class MD5 {

 public static void main(String[] args) {
     System.out.println(MD5.getMD5("123456"));
 }

 /**
  * Use md5 encoded code value
  *
  * @param sInput
  * clearly
  * @ return md5 encrypted password
  */
 public static String getMD5(String sInput) {

     String algorithm = "";
     if (sInput == null) {
         return "null";
     }
     try {
         algorithm = System.getProperty("MD5.algorithm", "MD5");
     } catch (SecurityException se) {
     }
     MessageDigest md = null;
     try {
         md = MessageDigest.getInstance(algorithm);
     } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
     }
     byte buffer[] = sInput.getBytes();

     for (int count = 0; count < sInput.length(); count++) {
         md.update(buffer, 0, count);
     }
     byte bDigest[] = md.digest();
     BigInteger bi = new BigInteger(bDigest);
     return (bi.toString(16));
 }
}
