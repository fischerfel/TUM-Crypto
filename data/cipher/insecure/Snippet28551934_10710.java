   import javax.crypto.Cipher;
   import javax.crypto.BadPaddingException;
   import javax.crypto.IllegalBlockSizeException;
   import javax.crypto.KeyGenerator;
   import java.security.Key;
   import java.security.InvalidKeyException;

   public class CryptTest {

        private static String algorithm = "DESede";
        private static Key key = null;
        private static Cipher cipher = null;

        private static void setUp() throws Exception {
            key = KeyGenerator.getInstance(algorithm).generateKey();
            cipher = Cipher.getInstance(algorithm);
        }

        public static void main(String[] args) 
           throws Exception {
            setUp();

            byte[] encryptionBytes = null;
            String input = "something";
            System.out.println("Entered: " + input);
            encryptionBytes = encrypt(input);
            String encryptedInput =  encryptionBytes.toString();
            System.out.println("encrypted: " + encryptedInput);


            System.out.println(
              "Recovered: " + decrypt(encryptionBytes));
        }

        private static byte[] encrypt(String input)
            throws InvalidKeyException, 
                   BadPaddingException,
                   IllegalBlockSizeException {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] inputBytes = input.getBytes();
            return cipher.doFinal(inputBytes);
        }

        private static String decrypt(byte[] encryptionBytes)
            throws InvalidKeyException, 
                   BadPaddingException,
                   IllegalBlockSizeException {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] recoveredBytes = 
              cipher.doFinal(encryptionBytes);
            String recovered = 
              new String(recoveredBytes);
            return recovered;
          }
   }
