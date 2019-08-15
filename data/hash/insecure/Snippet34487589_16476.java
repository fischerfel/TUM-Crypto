public class SecurityUtils {
     public final static int ITERATION_NUMBER = 1000;
     public static byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
           MessageDigest digest = MessageDigest.getInstance("SHA-1");
           digest.reset();
           digest.update(salt);
           byte[] input = digest.digest(password.getBytes("UTF-8"));
           for (int i = 0; i < iterationNb; i++) {
               digest.reset();
               input = digest.digest(input);
           }
           return input;
       }

}
