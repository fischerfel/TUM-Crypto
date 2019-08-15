import javax.crypto.*;
import java.util.Scanner;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class CIBAw
{    
    public static void main(String[] argv)  
    {
        Scanner input = new Scanner(System.in);
        Scanner File = new Scanner(System.in); 
        try
        {
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();
            Cipher desCipher;
            desCipher = Cipher.getInstance("DES");
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
            System.out.println("Encrypt a File");
            System.out.print("Enter a sentence:"); 
            //String file = File.nextLine(); 
            byte[] text = "".getBytes();
            System.out.println("" + new String(text));
            // Encrypt the text
            byte[] textEncrypted = desCipher.doFinal(text);
            System.out.println("File Encryted : " + textEncrypted);
            // Initialize the same cipher for decryption
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
            // Decrypt the text
            byte[] textDecrypted = desCipher.doFinal(textEncrypted);
            System.out.println("File Decryted : " +  new String(textDecrypted));

        }catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }catch(NoSuchPaddingException e)
        {
            e.printStackTrace();
        }catch(InvalidKeyException e)
        {
            e.printStackTrace();
        }catch(IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }catch(BadPaddingException e)
        {
            e.printStackTrace();
        } 

    }
}
