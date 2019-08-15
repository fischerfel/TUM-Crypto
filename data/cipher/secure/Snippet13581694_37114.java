import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import org.apache.commons.codec.binary.Base64;

public class AESDecrypt {
    private static final byte[] SALT = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private Cipher eCipher;
    private Cipher dCipher;

    AESDecrypt(String passPhrase) throws Exception {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
        SecretKey secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");

        eCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        eCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        dCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = eCipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        dCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    }

    public String decrypt(String encrypt) throws Exception {
        byte[] bytes = Base64.decodeBase64(encrypt);
        byte[] decrypted = decrypt(bytes);
        return new String(decrypted, "UTF8");
    }

    public byte[] decrypt(byte[] encrypt) throws Exception {
        return dCipher.doFinal(encrypt);
    }

    public static void main(String[] args) throws Exception {
        String passphrase = "PASSWORDPASSPHRASE";
        String encryptedPassword = "aUkbhjFebZ9VjJ44yptlBA==";        
        AESDecrypt aesDecrypt = new AESDecrypt(passphrase);
        String decryptedPassword = aesDecrypt.decrypt(encryptedPassword);
        System.out.println("decryptedPasswpord = " + decryptedPassword);
    }
}
