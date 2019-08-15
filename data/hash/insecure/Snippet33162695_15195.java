    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.security.SecureRandom;
    import java.util.Random;

    public class CouchPassword {

        public static void main(String[] args) throws NoSuchAlgorithmException {

            String password = "123456"; // your password        
            String salt = genSalt();
            password = password + salt;

            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(password.getBytes());
            byte byteData[] = md.digest();

            System.out.println("Password SHA:   " + byteToHexString(byteData) );
            System.out.println("Generated Salt: " + salt);
        }
        public static String genSalt() {
            Random ranGen = new SecureRandom();
            byte[] salt = new byte[16];
            ranGen.nextBytes(salt);
            return byteToHexString(salt);
        }
        public static String byteToHexString(byte[] b) {    
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
             sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
    }
    // There are libraries to help with generating SHAs and Hex Strings
    // but I have choosen not to use those here so the answer is more standalone.
