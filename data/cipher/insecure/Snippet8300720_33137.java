   import java.security.InvalidKeyException;
   import java.security.*;
   import javax.crypto.BadPaddingException;
   import javax.crypto.Cipher;
   import javax.crypto.IllegalBlockSizeException;
   import javax.crypto.SecretKey;
   import javax.crypto.SecretKeyFactory;
   import javax.crypto.spec.DESedeKeySpec;

      public class LocalEncrypter {

    private static String algorithm = "PBEWithMD5AndDES";
   //private static Key key = null;
    private static Cipher cipher = null;
    private static SecretKey key;

    private static void setUp() throws Exception {
        ///key = KeyGenerator.getInstance(algorithm).generateKey();
        SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
        String pass1 = "thisIsTheSecretKeyProvidedByMe";
        byte[] pass = pass1.getBytes(); 
        SecretKey key = factory.generateSecret(new DESedeKeySpec(pass));
        cipher = Cipher.getInstance(algorithm);
    }

    public static void main(String[] args) 
       throws Exception {
        setUp();

        byte[] encryptionBytes = null;
        String input = "1234";
        System.out.println("Entered: " + input);
        encryptionBytes = encrypt(input);
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
