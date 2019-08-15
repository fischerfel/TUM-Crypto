import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class EncryptionModule {

    /**
     * Encrypts
     */
    public static String encryptText(String plainText, SecretKey key) throws Exception{
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherT =  c.doFinal(plainText.getBytes());
        return new String(cipherT,"utf-8");
    }
    /**
     * Decrypts
     */
    public static String decryptText(String cipherText, SecretKey key) throws Exception{
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] clearT =  c.doFinal(cipherText.getBytes());
        return new String(clearT,"utf-8");
    }
}
