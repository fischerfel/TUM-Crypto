package crypto;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Crypto {
    public static byte[] encrypt(String message) throws Exception
    {
        String symmetricKey = "25Ae1f1711%z1 )1";
        SecretKeySpec aesKey = new SecretKeySpec(symmetricKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message.getBytes());
    }
    public static String decrypt(byte[] encryptedMessage) throws Exception
    {
        String symmetricKey = "25Ae1f1711%z1 )1";
        SecretKeySpec aesKey = new SecretKeySpec(symmetricKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(encryptedMessage));
    }
}
