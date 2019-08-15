    import java.security.InvalidKeyException;
    import java.security.NoSuchAlgorithmException;
    import java.security.spec.InvalidKeySpecException;

    import javax.crypto.BadPaddingException;
    import javax.crypto.Cipher;
    import javax.crypto.IllegalBlockSizeException;
    import javax.crypto.SecretKeyFactory;
    import javax.crypto.NoSuchPaddingException;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.DESedeKeySpec;
    import javax.xml.bind.DatatypeConverter;

    public class DESede {

     private static Cipher encryptCipher;
     private static Cipher decryptCipher;

     public static void main(String[] args) throws InvalidKeySpecException {
      try {


       String desKey = "0123456789abcdef0123456789abcdef0123456789abcdef"; // value from user
       byte[] keyBytes = DatatypeConverter.parseHexBinary(desKey);
       System.out.println((int)keyBytes.length);

       SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
       SecretKey key = factory.generateSecret(new DESedeKeySpec(keyBytes));

       encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
       encryptCipher.init(Cipher.ENCRYPT_MODE, key); //throwing Exception
       byte[] encryptedData = encryptData("Confidential data");

       decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
       decryptCipher.init(Cipher.DECRYPT_MODE, key);
       decryptData(encryptedData);

      } catch (NoSuchAlgorithmException e) {
       e.printStackTrace();
      } catch (NoSuchPaddingException e) {
       e.printStackTrace();
      } catch (InvalidKeyException e) {
       e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
       e.printStackTrace();
      } catch (BadPaddingException e) {
       e.printStackTrace();
      }

     }
