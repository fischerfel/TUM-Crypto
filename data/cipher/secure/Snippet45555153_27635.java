import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public class Main {

    private static final SecureRandom rand = new SecureRandom();

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024, rand);
        KeyPair kp = kpg.generateKeyPair();
        // Write out private key to file, PKCS8-encoded DER
        Files.write(Paths.get("privkey.der"), kp.getPrivate().getEncoded());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256, rand);
        SecretKey aesKey = kg.generateKey();

        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        c.init(Cipher.WRAP_MODE, kp.getPublic(), rand);
        byte[] wrappedKey = c.wrap(aesKey);

        // Write out wrapped key
        Files.write(Paths.get("wrappedkey"), wrappedKey);
    }
}
