import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.InvalidKeyException;

public class DESede {
    private static String algorithm = "DESede";
    private static Key key = null;
    private static SecretKey secretKey = null;
    private static Cipher cipher = null;
    private static DESede obj = new DESede();

    private DESede() {
        try {
            key = KeyGenerator.getInstance(algorithm).generateKey();
            KeyGenerator.getInstance(algorithm).getProvider();
            byte[] keyBytes = key.getEncoded();
            String keyFormat = key.getFormat();
            String keyAlgorithm = key.getAlgorithm();
            String keyString = new String(keyBytes);
            System.out.println("Key Format::" + keyFormat);
            System.out.println("Key Algorithm::" + keyAlgorithm);
            System.out.println("Key String::" + keyString);
            keyString.getBytes();
            secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "DESede");
            byte[] secretKeyBytes = key.getEncoded();
            String secretKeyFormat = key.getFormat();
            String secretKeyAlgorithm = key.getAlgorithm();
            String secretKeyString = new String(secretKeyBytes);
            System.out.println("Secret Key Format::" + secretKeyFormat);
            System.out.println("Secret Key Algorithm::" + secretKeyAlgorithm);
            System.out.println("Secret Key String::" + secretKeyString);
            String keyNewString = "bXŒ*êÂÕê›æOÄ’Îý‘ãô|8¶Ë1­";
            byte[] keyNewBytes = keyString.getBytes();
            secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "DESede");
            cipher = Cipher.getInstance(algorithm);
        } catch (Exception e) {
        }
    }

    public static DESede getInstance() {
        return obj;
    }

    public static byte[] encrypt(String input) throws InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        System.out.println("Inside encrypt()");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] inputBytes = input.getBytes();
        System.out.println("Exit encrypt()");
        return cipher.doFinal(inputBytes);
    }

    public static String decrypt(byte[] encryptionBytes)
            throws InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        System.out.println("Inside decrypt()");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
        String recovered = new String(recoveredBytes);
        System.out.println("Exiting decrypt()");
        return recovered;
    }

    public static void main(String args[]) throws InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        byte[] encryptedValue = DESede.encrypt("plz try encrypt and decrypt me");
        System.out.println("encryptedValue::" + encryptedValue);
        String decryptedValue = DESede.decrypt(encryptedValue);
        System.out.println("decryptedValue::" + decryptedValue);
    }
}
