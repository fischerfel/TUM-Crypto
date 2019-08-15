import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;
public class DESEncryptionDecryption {

private static Cipher encryptCipher;
private static Cipher decryptCipher;

public static void main(String[] args) throws InvalidKeySpecException {
try {

String desKey = "0123456789abcdef"; // value from user
byte[] keyBytes = DatatypeConverter.parseHexBinary(desKey);
System.out.println(keyBytes);

SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
SecretKey key = factory.generateSecret(new DESKeySpec(keyBytes));

encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
encryptCipher.init(Cipher.ENCRYPT_MODE, key);
byte[] encryptedData = encryptData("Confidential data"); //String from user

String s=encryptedData.toString();//String input to decrypt From user
byte[] bb=s.getBytes();

decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
decryptCipher.init(Cipher.DECRYPT_MODE, key);
decryptData(bb); //Exception

}catch (NoSuchAlgorithmException e) {
       e.printStackTrace();
      } catch (NoSuchPaddingException e) {
       e.printStackTrace();
      } catch (InvalidKeyException e) {
       e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
       e.printStackTrace();
      } catch (BadPaddingException e) {
       e.printStackTrace();
      }}
//method for encryption
private static byte[] encryptData(String data)
       throws IllegalBlockSizeException, BadPaddingException {
      System.out.println("Data Before Encryption :" + data);
      byte[] dataToEncrypt = data.getBytes();
      byte[] encryptedData = encryptCipher.doFinal(dataToEncrypt);
      System.out.println("Encryted Data: " + encryptedData);

      return encryptedData;
     }

//method for decryption

private static void decryptData(byte[] data)
throws IllegalBlockSizeException, BadPaddingException {
byte[] textDecrypted = decryptCipher.doFinal(data); //Exception trigered here
System.out.println("Decryted Data: " + new String(textDecrypted));
}}
