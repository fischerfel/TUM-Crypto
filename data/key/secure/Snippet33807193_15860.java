import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



public class AES {  
    private static SecretKeySpec secretKey ;
    private static byte[] key ;  
    private static String decryptedString;
    private static String encryptedString;

    public static void setKey(String myKey){ 
        MessageDigest sha = null;
        try {
                key = myKey.getBytes("UTF-8");

                sha = MessageDigest.getInstance("SHA-1");
                key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

       // System.out.println(new String(key,"UTF-8"));
            secretKey = new SecretKeySpec(key, "AES");


        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
    }

    public static String getDecryptedString() {
        return decryptedString;
    }

    public static void setDecryptedString(String decryptedString) {
        AES.decryptedString = decryptedString;
    }

    public static String getEncryptedString() {
        return encryptedString;
    }

    public static void setEncryptedString(String encryptedString) {
        AES.encryptedString = encryptedString;
    }

    public static String encrypt(String initVector, String strToEncrypt) throws InvalidAlgorithmParameterException
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);


            setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));

        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e)
        {

            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;

    }

    public static String decrypt(String initVector, String strToDecrypt) throws InvalidAlgorithmParameterException, UnsupportedEncodingException
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            setDecryptedString(new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt))));

        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e)
        {

            System.out.println("Error while decrypting: "+e.toString());

        }
        return null;
    }


    public static void main(String args[]) throws InvalidAlgorithmParameterException, UnsupportedEncodingException
    {
                String initVector = "Bw/+ctBXLE4=@f9t"; // 16 bytes IV
                final String strToEncrypt = "12345657";
                final String strPssword = "ViK#@Uef$#!BZFgAUW6qZiudgoQ==";
                AES.setKey(strPssword);

                AES.encrypt(initVector, strToEncrypt.trim());

                System.out.println("String to Encrypt: " + strToEncrypt); 
                System.out.println("Encrypted: " + AES.getEncryptedString());

                final String strToDecrypt =  AES.getEncryptedString();
                AES.decrypt(initVector, strToDecrypt.trim());

                System.out.println("String To Decrypt : " + strToDecrypt);
                System.out.println("Decrypted : " + AES.getDecryptedString());



    }

}
