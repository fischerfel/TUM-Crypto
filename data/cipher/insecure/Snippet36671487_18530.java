import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class Application
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        String textToEncrypt = "Hello World";
        String textToDecrypt;
        String textToDecryptAscii;
        String result;
        int operation;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //String key = "Bar12345Bar12345"; // 128 bit key
        String key = null;

        BASE64Encoder asciiEncoder = new BASE64Encoder();
        BASE64Decoder asciiDecoder = new BASE64Decoder();

        System.out.printf("Enter:\n1 for encryption\n2 for decryption\n\nChoice: ");
        operation = input.nextInt();
        input.nextLine();

        if (operation == 1)
        {
            try 
            {
                System.out.print("Enter a 128-bit key to be used for encryption: ");
                key = input.nextLine();

                if(key.length() != 16)
                {
                    while(key.length() != 16)
                    {
                        System.out.print("You need to enter a *128-bit* key: ");
                        key = input.nextLine();
                    }
                }

                System.out.printf("\n---------\n\nText to encrypt: ");
                textToEncrypt = input.nextLine();

                //Create key and cipher
                Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
                //Cipher cipher = Cipher.getInstance("AES");

                //encrypt the text
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());

                StringBuilder sb = new StringBuilder();
                for (byte b: encrypted)
                {
                    sb.append((char)b);
                }

                // the encrypted String
                String enc = sb.toString();
                //System.out.println("encrypted:" + enc);

                String asciiEncodedEncryptedResult = asciiEncoder.encodeBuffer(enc.getBytes());

                asciiEncodedEncryptedResult = asciiEncodedEncryptedResult.replace("\n", "").replace("\r", "");

                System.out.println("Encrypted text: " + asciiEncodedEncryptedResult);
                //System.out.printf("\n------------------------------\nDecrypted text: " + asciiEncodedEncryptedResult + "\n------------------------------\n\n\n");

            }
            catch(Exception e) 
            {
                e.printStackTrace();
            }
        }
        else if (operation == 2)
        {
            System.out.printf("\n---------\n\nText to decrypt: ");
            textToDecryptAscii = input.nextLine();

            System.out.print("Enter the 128-bit decryption key: ");
            key = input.nextLine();

            if(key.length() != 16)
            {
                while(key.length() != 16)
                {
                    System.out.print("You need to enter a *128-bit* key: ");
                    key = input.nextLine();
                }
            }

            byte[] decodedBytes = null;
            try
            {
                decodedBytes = asciiDecoder.decodeBuffer(textToDecryptAscii);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //System.out.println("decodedBytes " + new String(decodedBytes));

            textToDecrypt = new String(decodedBytes);

            //Convert the string to byte array
            //for decryption
            byte[] bb = new byte[textToDecrypt.length()];
            for (int i=0; i<textToDecrypt.length(); i++)
            {
                bb[i] = (byte) textToDecrypt.charAt(i);
            }

            //decrypt the text
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            try
            {
                cipher.init(Cipher.DECRYPT_MODE, aesKey);
            }
            catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String decrypted = null;
            try
            {
                decrypted = new String(cipher.doFinal(bb));
            }
            catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.printf("\n------------------------------\nDecrypted text: " + decrypted + "\n------------------------------\n\n\n");
        }
    }
}
