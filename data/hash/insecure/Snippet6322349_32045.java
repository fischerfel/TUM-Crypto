import java.io.*;
import java.security.*;

public class otp {

/**
 * @param args
 * @throws IOException 
 */
public static void main(String[] args) throws IOException {

    System.out.println("Please enter your username:");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String username = br.readLine();
    System.out.println("Please enter your password:");
    String password = br.readLine();

    try {
         MessageDigest md = MessageDigest.getInstance("SHA1");

         String input = password;
         md.update(input.getBytes()); 
         byte[] output = md.digest();
         System.out.println();
         System.out.println("SHA1(\""+input+"\") =");
         System.out.println("   "+bytesToHex(output));


      } catch (Exception e) {
         System.out.println("Exception: "+e);
      }
   }
   public static String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer buf = new StringBuffer();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();        
}
}
