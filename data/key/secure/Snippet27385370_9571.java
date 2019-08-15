import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt1
{    
public static void main(String[] argv) {

    try{

        //KeyGenerator keygenerator = KeyGenerator.getInstance("AES");

        //SecretKey myAesKey = keygenerator.generateKey(key);

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

        // Initialize the same cipher for decryption
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);

        // Decrypt the text
        byte[] textDecrypted = aesCipher.doFinal(textEncrypted);

        System.out.println("Text Decryted : " + new String(textDecrypted));

    }catch(NoSuchAlgorithmException e){
        e.printStackTrace();
    }catch(NoSuchPaddingException e){
        e.printStackTrace();
    }catch(InvalidKeyException e){
        e.printStackTrace();
    }catch(IllegalBlockSizeException e){
        e.printStackTrace();
    }catch(BadPaddingException e){
        e.printStackTrace();
    } 

}
}
