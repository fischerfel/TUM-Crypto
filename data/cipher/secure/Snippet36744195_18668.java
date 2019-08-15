package implementaes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec; 
import javax.crypto.spec.SecretKeySpec;

public class Aesaesaes
{
    public static void main(String[] args)
    {
        try
        {
                //Lookup a key generator for the AES cipher
                        KeyGenerator kg = KeyGenerator.getInstance("AES");
            SecretKey key = kg.generateKey();

            SecretKeySpec keySpec = new
                        SecretKeySpec(key.getEncoded(), "AES");     
                //Lookup an instance of a AES cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                //initialize IV  manually

                byte[] ivBytes = new byte[] {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

                //create IvParameterSpecobject

                IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);     

               //Initialize the cipher using the secter key

            cipher.init(Cipher.ENCRYPT_MODE, keySpec,ivSpec);

                String plainText = "This is a secret!";



            byte[] cipherText = cipher.doFinal(plainText.getBytes());

            System.out.println("Resulting Cipher Text:\n");
            for(int i=0;i<cipherText.length;i++)
            {
                System.out.print(cipherText[i] + " ");
            }
            System.out.println("");



        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
