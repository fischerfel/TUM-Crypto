import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public abstract class AESDecrypter {

    public static String decrypt(String encryptedString, String key) {
        try {
            SecretKeySpec keySpec = null;
            Cipher decryptCipher = null;
            //----
            SecretKeySpec keySpec = null;
        Cipher decryptCipher = null;
        //----
        keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        System.out.println(keySpec.toString());
        decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, keySpec);
        System.out.println("End decrypt");
        return new String(decryptCipher.doFinal(Base64.decodeBase64(encryptedString.getBytes("UTF-8"))), "UTF-8");
    }
}
