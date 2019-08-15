import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class Application { 

    public Application() {
        // TODO Auto-generated constructor stub
    }

    public static void main ( String[] args ) {
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
            e1.printStackTrace();
        } catch (NoSuchPaddingException e1) {
            e1.printStackTrace();
        }

        //String key = "Bar12345Bar12345"; // 128 bit key
        String key = null;
        //byte[] key = null;

        //BASE64Encoder asciiEncoder = new BASE64Encoder();
        //BASE64Decoder asciiDecoder = new BASE64Decoder();

        //System.out.printf("Enter:\n1 for encryption\n2 for decryption\n\nChoice: ");
        //operation = input.nextInt();
        //input.nextLine();

        try { 
            System.out.print("Enter a 128-bit key to be used for encryption: ");
            key = input.nextLine();

            if ( key.length() != 16 ) {
                while ( key.length() != 16 ) {
                    System.out.print("You need to enter a *128-bit* key: ");
                    key = input.nextLine();
                }
            }
            System.out.println ( "128-bit encryption key.......................["+key+"] length ["+key.length ()+"]");

            System.out.printf ( "Text to encrypt..............................[");
            //System.out.printf("\n---------\n\nText to encrypt: ");
            textToEncrypt = input.nextLine();
            System.out.println ( "Text to encrypt..............................["+textToEncrypt+"] length ["+textToEncrypt.length ()+"]");

            //Create key and cipher
            Key aesKey = new SecretKeySpec(key.trim().getBytes(), "AES");
            //Cipher cipher = Cipher.getInstance("AES");

            //encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes ());

            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char)b);
            }

            // the encrypted String
            String enc = sb.toString();
            System.out.println ( "Encrypted text...............................["+enc+"] length ["+enc.length ()+"]");
            //System.out.println("encrypted:" + enc);

            //String asciiEncodedEncryptedResult = asciiEncoder.encodeBuffer(enc.getBytes()).trim ();
            String asciiEncodedEncryptedResult = enc.trim ();
            System.out.println ( "Encoded text.................................["+asciiEncodedEncryptedResult+"] length ["+asciiEncodedEncryptedResult.length ()+"]");

            //asciiEncodedEncryptedResult = asciiEncodedEncryptedResult.replace("\n", "").replace("\r", "");
            asciiEncodedEncryptedResult = asciiEncodedEncryptedResult.trim ();

            System.out.println ( "Encrypted text...............................["+asciiEncodedEncryptedResult+"] length ["+asciiEncodedEncryptedResult.length ()+"]");


            //byte[] decodedBytes = null;
            //try {
            //    decodedBytes = asciiDecoder.decodeBuffer(asciiEncodedEncryptedResult);
            //} 
            //catch (IOException e1) {
            //    e1.printStackTrace();
            //}
            //System.out.println ( "Decoded Bytes................................["+decodedBytes+"] length ["+decodedBytes.length+"]");

            //textToDecrypt = new String(decodedBytes);
            textToDecrypt = asciiEncodedEncryptedResult;

            System.out.println ( "Text to Decrypt..............................["+textToDecrypt+"] length ["+textToDecrypt.length()+"]");

            //Convert the string to byte array
            //for decryption
            byte[] bb = new byte[textToDecrypt.length()];
            for ( int i=0; i<textToDecrypt.length(); i++ ) {
                bb[i] = (byte) textToDecrypt.charAt(i);
            }

            //decrypt the text
            //Key aesKey = null;
            String decrypted = null;
            try {
                //aesKey = new SecretKeySpec(key.trim ().getBytes (), "AES");
                cipher.init(Cipher.DECRYPT_MODE, aesKey);
                decrypted = new String(cipher.doFinal(bb));
            }
            catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            catch (BadPaddingException e) {
                e.printStackTrace();
            }
            catch ( Exception ltheXcp ) { 
                ltheXcp.printStackTrace ();
            }

            if ( decrypted != null ) {
                System.out.println ( "Decrypted text...............................["+decrypted+"] length ["+decrypted.length ()+"]");
            }
            else { 
                System.out.println ( "Decrypted text...............................["+decrypted+"] length []");
            }

        }
        catch ( Exception ltheXcp ) { 
            ltheXcp.printStackTrace ();
        }

    }
}
