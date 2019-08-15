import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.Cipher;

public class Test {

    private static final String RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

    public static void main(String[] args) {
        String data = "Hello World";

        KeyPair kp = generateRSAKeyPair();

        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();

        byte[] encryptedValue = encryptRSA(publicKey, data.getBytes());
        byte[] decrytpedValue = decryptRSA(privateKey, encryptedValue);

        String decryptedData = new String(decrytpedValue);

        System.out.println(decryptedData);
    }

    public static KeyPair generateRSAKeyPair() {
        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom rnd = new SecureRandom();
            keyGen.initialize(2048, rnd);
            KeyPair keyPair = keyGen.genKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptRSA(Key key, byte[] data) {
        byte[] cipherText = null;
        try {
            final Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static byte[] decryptRSA(Key key, byte[] data) {
        byte[] decryptedText = null;
        try {
            final Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedText = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

}
