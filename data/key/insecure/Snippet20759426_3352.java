import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class AESFile 
{
private static String algorithm = "AES";
private static byte[] keyValue=new byte[] {'0','2','3','4','5','6','7','8','9','1','2','3','4','5','6','7'};// your key

    // Performs Encryption
    public static String encrypt(String plainText) throws Exception 
    {
            Key key = generateKey();
            Cipher chiper = Cipher.getInstance(algorithm);
            chiper.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = chiper.doFinal(plainText.getBytes());
            String encryptedValue = new BASE64Encoder().encode(encVal);
            return encryptedValue;
    }

    // Performs decryption
    public static String decrypt(String encryptedText) throws Exception 
    {
            // generate key 
            Key key = generateKey();
            Cipher chiper = Cipher.getInstance(algorithm);
            chiper.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
            byte[] decValue = chiper.doFinal(decordedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
    }

//generateKey() is used to generate a secret key for AES algorithm
    private static Key generateKey() throws Exception 
    {
            Key key = new SecretKeySpec(keyValue, algorithm);
            return key;
    }

    // performs encryption & decryption 
    public static void main(String[] args) throws Exception 
    {
        FileReader file = new FileReader("C://myprograms//plaintext.txt");
        BufferedReader reader = new BufferedReader(file);
        String text = "";
        String line = reader.readLine();
    while(line!= null)
        {
            text += line;
    line = reader.readLine();
        }
        reader.close();
    System.out.println(text);

            String plainText = text;
            String encryptedText = AESFile.encrypt(plainText);
            String decryptedText = AESFile.decrypt(encryptedText);

            System.out.println("Plain Text : " + plainText);
            System.out.println("Encrypted Text : " + encryptedText);
            System.out.println("Decrypted Text : " + decryptedText);
    }
}
