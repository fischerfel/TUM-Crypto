import java.security.*;
import javax.crypto.*;

Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
aes.init(Cipher.ENCRYPT_MODE, key);
byte[] ciphertext = aes.doFinal("my cleartext".getBytes());

aes.init(Cipher.DECRYPT_MODE, key);
String cleartext = new String(aes.doFinal(ciphertext));
