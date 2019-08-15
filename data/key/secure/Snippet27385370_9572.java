import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class Encrypt2 {

public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
    // TODO Auto-generated method stub

    Encrypt();
    Decrypt();

}


public static void Encrypt() throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{

    Scanner input = new Scanner(System.in);

    System.out.println("Please enter a message you'd like to encrypt");
    String plaintext = input.nextLine();

    System.out.println("Please enter a 16 digit password: ");
    String pass = input.nextLine();

    byte[] key = pass.getBytes();
    SecretKeySpec aesKey = new SecretKeySpec(key, "AES");

    Cipher aesCipher;

    // Create the cipher 
    aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

    // Initialize the cipher for encryption
    aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);

    //sensitive information
    byte[] text = plaintext.getBytes();

    System.out.println("Text [Byte Format] : " + text);
    System.out.println("Text : " + new String(text));

    // Encrypt the text
    byte[] textEncrypted = aesCipher.doFinal(text);

    System.out.println("Text Encryted : " + textEncrypted);

}
public static void Decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{

     Scanner input = new Scanner(System.in);

     System.out.println("Please enter a message you'd like to decrypt");
        String ciphertext = input.nextLine();
        byte[] textEncrypted = ciphertext.getBytes();


        System.out.println("Please enter a 16 digit password: ");
        String passw = input.nextLine();


        byte[] key = passw.getBytes();
        SecretKeySpec aesKey = new SecretKeySpec(key, "AES");

        Cipher aesCipher;

        // Create the cipher 
        aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        String pass = input.nextLine(); // Initialize the same cipher for decryption
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);

        // Decrypt the text
        byte[] textDecrypted = aesCipher.doFinal(textEncrypted);

        System.out.println("Text Decryted : " + new String(textDecrypted));
}
}
