import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;


public class EncryptFile{
    private static final String FILE_IN = "./EncryptFile.java";
    private static final String FILE_ENCR = "./EncryptFile_encr.java";
    private static final String FILE_DECR = "./EncryptFile_decr.java";
     public static void main(String []args){
        try
        {
            Encryption("passwordisnottheactual", Files.readAllBytes(Paths.get(FILE_IN)));
            Decryption("passwordisnottheactual");

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
     }
     private static void Encryption(String Key, byte[] byteArray) throws Exception
     {
        // Decode the base64 encoded Key
        byte[] decodedKey = Base64.getDecoder().decode(Key);
        // Rebuild the key using SecretKeySpec
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 

        // Cipher gets AES Algorithm instance
        Cipher AesCipher = Cipher.getInstance("AES");

        //Initialize AesCipher with Encryption Mode, Our Key and A ?SecureRandom?
        AesCipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());
        byte[] byteCipherText = AesCipher.doFinal(byteArray);

        //Write Bytes To File
        Files.write(Paths.get(FILE_ENCR), byteCipherText);


     }
     private static void Decryption(String Key) throws Exception
     {
        //Ddecode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(Key);
        //Rebuild key using SecretKeySpec
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 

        //Read All The Bytes From The File
        byte[] cipherText = Files.readAllBytes(Paths.get(FILE_ENCR));

        //Cipher gets AES Algorithm Instance
        Cipher AesCipher = Cipher.getInstance("AES");

        //Initialize it in Decrypt mode, with our Key, and a ?SecureRandom?
        AesCipher.init(Cipher.DECRYPT_MODE, secretKey, new SecureRandom());

        byte[] bytePlainText = AesCipher.doFinal(cipherText);
        Files.write(Paths.get(FILE_DECR), bytePlainText);
     }
}
