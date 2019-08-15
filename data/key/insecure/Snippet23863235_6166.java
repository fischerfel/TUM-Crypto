 import java.security.Key;

        import javax.crypto.Cipher;
        import javax.crypto.spec.SecretKeySpec;

        import sun.misc.BASE64Decoder;
        import sun.misc.BASE64Encoder;

        public class Decrypt256bit {


        private static Key key;

        private static Cipher cipher;

        static {
            key = new SecretKeySpec("P@ssw0Rd!@#**&&&P@ssw0Rd!@#**&&&".getBytes(), "AES");
            try {
                cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING","SunJCE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static String encryptData(String plainText) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] encrypted = cipher.doFinal(plainText.getBytes());
                return new BASE64Encoder().encode(encrypted);
            } catch (Exception e) {

                throw new IllegalArgumentException(e);
            }
        }


        //For testing purpose - to be deleted
        public static String decryptData(String encryptedValue) {
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
                int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
                System.out.println("Length==="+maxKeyLen);
                return new String(cipher.doFinal(decordedValue));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        public static void main(String... a) {
            //String enc = encryptData("TPASU~TPAGU");
            //System.out.println("Encrypted text==="+enc);      
        }

        }
