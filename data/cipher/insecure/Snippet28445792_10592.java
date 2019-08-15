import java.util.Scanner;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKey;
/**

 */
public class ENCRY
{
    public static void main(String[] args) 
    {

        try{
            do{
                Scanner input = new Scanner(System.in);
                System.out.println("Please select an Option");
                System.out.println("Encrypt the Sentence");
                int File =input.nextInt();
                System.out.println("--------------------");
                System.out.print("Enter the sentence:");
                String s = input.next();

                KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
                SecretKey myDesKey = keygenerator.generateKey();
                Cipher desCipher;
                desCipher = Cipher.getInstance("DES");
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                byte[] text = s.getBytes();
                System.out.println("" + new String(text));


                byte[] textEncrypted = desCipher.doFinal(text);
                System.out.println("sentence Encryted : " + textEncrypted);
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);

                byte[] textDecrypted = desCipher.doFinal(textEncrypted);
                System.out.println("sentence Decryted : " +  new String(textDecrypted));



while()



        }catch(NoSuchAlgorithmException S)
        {
            S.printStackTrace();
        }catch(NoSuchPaddingException S)
        {
            S.printStackTrace();
        }catch(InvalidKeyException S)
        {
            S.printStackTrace();
        }catch(IllegalBlockSizeException S)
        {
            S.printStackTrace();
        }catch(BadPaddingException S)
        {
            S.printStackTrace();
        } 

    }
}
