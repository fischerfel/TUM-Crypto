import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Gen_DESAES_key{


byte[] message = "Hello World".getBytes();

KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
SecretKey desKey = keygenerator.generateKey();

Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
desCipher.init(Cipher.ENCRYPT_MODE,deskey);

byte[] encryptedMessage = desCipher.doFinal(message);
}
