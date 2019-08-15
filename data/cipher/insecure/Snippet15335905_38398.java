import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

 public class AESEncryptionDecryptionTest {

   private static final String ALGORITHM       = "AES";
   private static final String myEncryptionKey = "OIXQUULC7khaJzzOOHRqgw==";
   private static final String UNICODE_FORMAT  = "UTF8";

   public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);  
        byte[] encValue = c.doFinal(valueToEnc.getBytes(UNICODE_FORMAT));
        String encryptedValue = new Hex().encodeHexString(encValue);
        return encryptedValue;
   }

   public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new Hex().decode(encryptedValue.getBytes());
        byte[] decValue = c.doFinal(decordedValue);//////////LINE 50
        String decryptedValue = new String(decValue);
        return decryptedValue;
   }

   private static Key generateKey() throws Exception {
        byte[] keyAsBytes;
        keyAsBytes = myEncryptionKey.getBytes();
        Key key = new SecretKeySpec(keyAsBytes, ALGORITHM);
        return key;
   }

   public static void main(String[] args) throws Exception {

        String value = "PFN123";
        String valueEnc = AESEncryptionDecryptionTest.encrypt(value);
        String valueDec = AESEncryptionDecryptionTest.decrypt(valueEnc);

        System.out.println("Plain Text : " + value);
        System.out.println("Encrypted : " + valueEnc);
        System.out.println("Decrypted : " + valueDec);
   }

}
