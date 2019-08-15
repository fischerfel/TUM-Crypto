import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.SecureRandom;
import java.security.Security;

public class Scratch {
    public static void main(String[] args) throws Exception {
        int keyLength = 128;

        Security.addProvider(new BouncyCastleProvider());

        String password = "password";

        SecureRandom randomGenerator = new SecureRandom();
        byte[] salt = randomGenerator.generateSeed(128 / 8);
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 872791, keyLength);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        SecretKey passwordKey = secretKeyFactory.generateSecret(keySpec);
        System.out.println("passwordKey: " + passwordKey);
        System.out.println("passwordKey.getEncoded(): " + Arrays.toString(passwordKey.getEncoded()));
        System.out.println("passwordKey.getEncoded().length: " + passwordKey.getEncoded().length);
        System.out.println("passwordKey.getFormat():" + passwordKey.getFormat());
        System.out.println("passwordKey.getAlgorithm(): " + passwordKey.getAlgorithm());

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        PBEParameterSpec parSpec = new PBEParameterSpec(salt, 872791);
        cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parSpec);
    }
}
