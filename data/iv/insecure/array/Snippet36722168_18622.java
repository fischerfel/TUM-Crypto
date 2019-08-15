  import java.nio.file.Files;
  import java.nio.file.Paths;
  import javax.crypto.*;
  import javax.crypto.spec.IvParameterSpec;
  import javax.crypto.spec.SecretKeySpec;

  import java.util.*;

  public class Encrypter {

  public static void main(String[] args) throws Exception {
  String FileName = "encryptedtext.txt";
  String FileName2 = "decryptedtext.txt";

  Scanner input = new Scanner(System.in);

  System.out.println("Enter your 16 character key here:");
  String EncryptionKey = input.next();
  byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
  IvParameterSpec ivspec = new IvParameterSpec(iv);


  KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
  KeyGen.init(128);


  Cipher AesCipher = Cipher.getInstance("AES/CFB/NoPadding");
  System.out.println("Enter text to encrypt or decrypt:");
  String Text = input.next();

  System.out.println("Do you want to encrypt or decrypt (e/d)");
  String answer = input.next();
  if (answer.equalsIgnoreCase("e")) {

   byte[] byteKey = (EncryptionKey.getBytes());
   byte[] byteText = (Text).getBytes();
   SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "AES");
   AesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
   AesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec); // ERROR LINE
   byte[] byteCipherText = AesCipher.doFinal(byteText);
   Files.write(Paths.get(FileName), byteCipherText);


  } else if (answer.equalsIgnoreCase("d")) {

   byte[] byteKey = (EncryptionKey.getBytes());
   byte[] byteText = (Text).getBytes();
   byte[] cipherText = Files.readAllBytes(Paths.get(FileName));

   SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "AES");
   AesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec); // ERROR LINE
   byte[] bytePlainText = AesCipher.doFinal(cipherText);
   Files.write(Paths.get(FileName2), bytePlainText);
  }
 }

}
