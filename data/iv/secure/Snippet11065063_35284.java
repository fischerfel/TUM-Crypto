import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class SimplestTest {
    public static void main(String[] args) throws Exception {
        SecureRandom rnd = new SecureRandom();

        String text = "Hello, my dear! ... " + System.getProperty("user.home");
        byte[] textData = text.getBytes();

        IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey k = generator.generateKey();

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, k, iv);
        c.update(textData);
        byte[] data = c.doFinal();

        System.out.println("E: " + data.length);

        c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, k, iv);
        c.update(data);

        System.out.println("R: " + c.doFinal().length);
    }

}
