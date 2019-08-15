import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CryptoUtil {

    private static final String AES_TECHNIQUE = "AES/CBC/PKCS5PADDING";

    public String encryptUsingAES(String key, String initVector, String sourceString) {

        String encryptedString  = null;

        try {

            IvParameterSpec iv = new IvParameterSpec( initVector.getBytes("UTF-8") );
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(AES_TECHNIQUE);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(sourceString.getBytes());

            encryptedString = Base64.encodeBase64String(encrypted);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return encryptedString;
    }

    public String decryptUsingAES(String key, String initVector, String encryptedString) {

        String decryptedString  = null;

        try {

            IvParameterSpec iv = new IvParameterSpec( initVector.getBytes("UTF-8") );
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(AES_TECHNIQUE);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encryptedString));

            decryptedString = new String(original);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return decryptedString;
    }

    public static void main(String[] args) {

        CryptoUtil cUtil =  new CryptoUtil();

        String sourceString = "abcd";
        String encrString = cUtil.encryptUsingAES("shahezadshahezad", "shahezadshahezad", sourceString);
        String decrString = cUtil.decryptUsingAES("shahezadshahezad", "shahezadshahezad", encrString);

        System.out.println("Source String : " + sourceString);
        System.out.println("Encrypted String : " + encrString);
        System.out.println("Decrypted String : " + decrString);
    }
}
