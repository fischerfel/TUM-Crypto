import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;    
import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.KeyGenerator;  
import javax.crypto.NoSuchPaddingException;  
import javax.crypto.SecretKey;

public class DESEncryptionDecryption {

private static Cipher encryptCipher;  
private static Cipher decryptCipher; 
public static void main(String[] args) {  
try {
KeyGenerator keygenerator = KeyGenerator.getInstance("DES");  
SecretKey secretKey = keygenerator.generateKey();  

encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");  
encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);  
byte[] encryptedData = encryptData("Classified Information!");  

decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");  
decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);  
decryptData(encryptedData);
}}}
