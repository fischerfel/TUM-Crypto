import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {
public static void main(String[] args) throws Exception {
    char[] secret = "Secret password".toCharArray();
    byte[] salt = "Salty salt".getBytes();
    int iterations = 10000;
    int keyLengthBits = 128;
    for (int i=0; i<20; i++) {
        long timeStart = System.nanoTime();
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec spec = new PBEKeySpec(secret, salt, iterations, keyLengthBits);
        SecretKey key = skf.generateSecret(spec);
        long timeEnd = System.nanoTime();
        System.out.println((timeEnd-timeStart)/1000000 + "ms");
    }
}
}
