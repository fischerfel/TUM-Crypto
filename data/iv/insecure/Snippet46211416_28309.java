import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

    import javax.crypto.Cipher;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;
    import java.util.Base64;

    public enum AESUtil {
        ;
        private static final String ENCRYPTION_KEY = "RwcmlVpg";
        private static final String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";

        public static String encrypt(String src) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
                Base64.Encoder encoder = Base64.getEncoder();
                return encoder.encodeToString(cipher.doFinal(src.getBytes()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String decrypt(String src) {
            String decrypted = "";
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
                Base64.Decoder decoder = Base64.getDecoder();
                decrypted = new String(cipher.doFinal(decoder.decode(src)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return decrypted;
        }

        static AlgorithmParameterSpec makeIv() {
            try {
                return new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }

        static Key makeKey() {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] key = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));
                return new SecretKeySpec(key, "AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
