import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class MyClass {

    public static void main(String[] args) {
        String data = "Arnab C";
        final String enc = DarKnight.getEncrypted(data);
        System.out.println("Encrypted : " + enc);
        System.out.println("Decrypted : " + DarKnight.getDecrypted(enc));
    }

    static class DarKnight {

        private static final String ALGORITHM = "AES";

        private static final byte[] SALT = "tHeApAcHe6410111".getBytes();// THE KEY MUST BE SAME
        private static final String X = DarKnight.class.getSimpleName();

        static String getEncrypted(String plainText) {

            if (plainText == null) {
                return null;
            }

            Key salt = getSalt();

            try {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, salt);
                byte[] encodedValue = cipher.doFinal(plainText.getBytes());
                return Base64.encode(encodedValue);
            } catch (Exception e) {
                e.printStackTrace();
            }

            throw new IllegalArgumentException("Failed to encrypt data");
        }

        public static String getDecrypted(String encodedText) {

            if (encodedText == null) {
                return null;
            }

            Key salt = getSalt();
            try {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, salt);
                byte[] decodedValue = Base64.decode(encodedText);
                byte[] decValue = cipher.doFinal(decodedValue);
                return new String(decValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        static Key getSalt() {
            return new SecretKeySpec(SALT, ALGORITHM);
        }

    }
}
