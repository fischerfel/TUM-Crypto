import java.io.*;
import java.security.*;
import javax.crypto.*;
import sun.misc.BASE64Encoder;


public class RCCC4 {
public static void main(String[] args) {
    String strDataToEncrypt = new String();
    String strCipherText = new String();
    String strDecryptedText = new String();

    try{ 
    KeyGenerator keyGen = KeyGenerator.getInstance("RC4");
    SecretKey secretKey = keyGen.generateKey();
    Cipher aesCipher = Cipher.getInstance("RC4");
    aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);
    strDataToEncrypt = "Hello World of Encryption using RC4 ";
    byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
    byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt); 
    strCipherText = new BASE64Encoder().encode(byteCipherText);
    System.out.println("Cipher Text generated using RC4 is " +strCipherText);
    aesCipher.init(Cipher.DECRYPT_MODE,secretKey,aesCipher.getParameters());
    byte[] byteDecryptedText = aesCipher.doFinal(byteCipherText);
    strDecryptedText = new String(byteDecryptedText);
    System.out.println(" Decrypted Text message is " +strDecryptedText);
    }
    catch (NoSuchPaddingException noSuchPad)
        {
            System.out.println(" No Such Padding exists " + noSuchPad);
        }

    catch (InvalidKeyException invalidKey)
        {
                System.out.println(" Invalid Key " + invalidKey);
        }

    catch (BadPaddingException badPadding)
        {
                System.out.println(" Bad Padding " + badPadding);
        }

    catch (IllegalBlockSizeException illegalBlockSize)
        {
                System.out.println(" Illegal Block Size " + illegalBlockSize);
        }

    catch (InvalidAlgorithmParameterException invalidParam)
        {
                System.out.println(" Invalid Parameter " + invalidParam);
        }

}
   }
