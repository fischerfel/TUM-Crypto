import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

...

byte[] salt = new byte[8];
Random rand = new Random();
rand.nextBytes(salt);

PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAndTwofish-CBC");
SecretKey key = keyFactory.generateSecret(keySpec);
PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 1000);

Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwofish-CBC");
cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
byte[] ciphertext = cipher.doFinal(plaintext);
