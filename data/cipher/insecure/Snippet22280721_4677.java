import java.security.spec.*;
import javax.crypto.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESedeEncryption {
public static void main(String[] args) {

SecretKey k1 = generateDESkey();
SecretKey k2 = generateDESkey();

String firstEncryption = desEncryption("plaintext", k1);
System.out.println("firstEncryption Value : "+firstEncryption);
String decryption = desDecryption(firstEncryption, k2);
System.out.println("decryption Value : "+decryption);
String secondEncryption = desEncryption(decryption, k1);
System.out.println("secondEncryption Value : "+secondEncryption);

}

public static SecretKey generateDESkey() {
KeyGenerator keyGen = null;
try {
    keyGen = KeyGenerator.getInstance("DESede");
} catch (Exception ex) {     
}
keyGen.init(112); // key length 56
SecretKey secretKey = keyGen.generateKey();
return secretKey;
}

public static String desEncryption(String strToEncrypt, SecretKey desKey) {
try {
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, desKey);
    BASE64Encoder base64encoder = new BASE64Encoder();
    byte[] encryptedText = cipher.doFinal(strToEncrypt.getBytes());
    String encryptedString =base64encoder.encode(encryptedText);
    return encryptedString;
} catch (Exception ex) {
}
return null;
}

public static String desDecryption(String strToDecrypt, SecretKey desKey) {
try {
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, desKey);
    BASE64Decoder base64decoder = new BASE64Decoder();
    byte[] encryptedText = base64decoder.decodeBuffer(strToDecrypt);
    byte[] plainText = cipher.doFinal(encryptedText);
    String decryptedString= bytes2String(plainText);
    return decryptedString;
} catch (Exception ex) {
}
return null;
}

 private static String bytes2String(byte[] bytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i <bytes.length; i++) {
        stringBuffer.append((char) bytes[i]);
    }
    return stringBuffer.toString();
}
}
