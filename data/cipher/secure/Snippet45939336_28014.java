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

        Security.addProvider(new BouncyCastleProvider());

        String password = "password";

        SecureRandom randomGenerator = new SecureRandom();
        byte[] salt = randomGenerator.generateSeed(256);
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey passwordKey = f.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        PBEParameterSpec parSpec = new PBEParameterSpec(salt, 65536);
        cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parSpec);
    }
}
