import java.io.*;
import java.security.*;
import javax.crypto.*;

public class EncryptAndDecrypt
{


    public static void main(String[] args) 
    {
        try{
        KeyPairGenerator keygenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keygenerator.initialize(1024, random);

        KeyPair keypair = keygenerator.generateKeyPair();
        PrivateKey privateKey = keypair.getPrivate();
        PublicKey publicKey = keypair.getPublic();
        Cipher cipher = Cipher.getInstance("RSA");

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\Desktop\\testing.txt")));
        String st="", str="";
        while ((st = br.readLine()) != null)
        {
            str += st + " ";
        }

        byte[] cleartext = null;
        cleartext = str.getBytes();
        byte[] ciphertext = null;
        ciphertext = cipher.doFinal(cleartext);
        System.out.println("the encrypted text is: " + ciphertext.toString());

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cleartext1 = cipher.doFinal(ciphertext);
        System.out.println("the decrypted cleartext is: " + new String(cleartext1));
        }catch(Exception e){e.printStackTrace();}
    }

}
