import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESedeEncryption {
public static void main(String[] args) {

SecretKey k1 = generateDESkey();
SecretKey k2 = generateDESkey();
SecretKey k3 = generateDESkey();

String firstEncryption = desEncryption("plaintext", k1);
String decryption = desDecryption(firstEncryption, k2);
String secondEncryption = desEncryption(decryption, k3);
System.out.println("secondEncryption: "+secondEncryption);
System.out.println("firstEncryption: "+firstEncryption);
System.out.println("decryption: "+decryption);

}

public static SecretKey generateDESkey() {
KeyGenerator keyGen = null;
try {
    keyGen = KeyGenerator.getInstance("DESede");
} catch (Exception ex) {
  ex.printStackTrace();
}
keyGen.init(168); // key length 112 for two keys, 168 for three keys
SecretKey secretKey = keyGen.generateKey();
return secretKey;
}

public static String desEncryption(String strToEncrypt, SecretKey desKey) {
try {
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, desKey);
    String encryptedString = new      BASE64Encoder().encode(cipher.doFinal(strToEncrypt.getBytes()));
    return encryptedString;


} catch (Exception ex) {
  ex.printStackTrace();
}
return null;
}

public static String desDecryption(String strToDecrypt, SecretKey desKey) {
try {
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, desKey);
    String decryptedString = new String(cipher.doFinal(new     BASE64Decoder().decodeBuffer(strToDecrypt)));
    return decryptedString;


} 
catch (Exception ex) {
  ex.printStackTrace();
}
return null;
}
}
