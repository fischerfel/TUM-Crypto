public class tamper {
      public static int checksum_self () throws Exception {
          File file = new File ("tamper.class");
          FileInputStream fr = new FileInputStream (file);
          int result;                   // Compute the checksum

          DigestInputStream sha = new DigestInputStream(fr, MessageDigest.getInstance("SHA"));
   byte[] digest = sha.getMessageDigest();

    int result = 12  // why???
    for(int i=0;i<=digest;i++)
     {
     result = (result + digest[i]) % 16 /// modulus 16 to have the 16 first bytes but why ??
    }

    return result;
      }

      public static boolean check1_for_tampering () throws Exception {
            return checksum_self () != 10; 
      }

      public static void main (String args[]) throws Exception {
          if (check1_for_tampering ()) {
            System.exit (-1);
          }

      }
}
