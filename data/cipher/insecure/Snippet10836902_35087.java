 import java.security.Key;
 import javax.crypto.Cipher;
 import javax.crypto.spec.SecretKeySpec;
 import sun.misc.BASE64Decoder;
 import sun.misc.BASE64Encoder;

 public class AESEncryptionDecryptionTest {

   private static final String ALGORITHM       = "AES";
   private static final String myEncryptionKey = "ThisIsFoundation";
   private static final String UNICODE_FORMAT  = "UTF8";

   public static String encrypt(String valueToEnc) throws Exception {
 Key key = generateKey();
 Cipher c = Cipher.getInstance(ALGORITHM);
 c.init(Cipher.ENCRYPT_MODE, key);  
 byte[] encValue = c.doFinal(valueToEnc.getBytes());
 String encryptedValue = new BASE64Encoder().encode(encValue);
 return encryptedValue;
   }


private static Key generateKey() throws Exception {
byte[] keyAsBytes;
keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
Key key = new SecretKeySpec(keyAsBytes, ALGORITHM);
return key;
}


}
