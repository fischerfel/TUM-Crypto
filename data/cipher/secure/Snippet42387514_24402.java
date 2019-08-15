package com.ust;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;    
import org.apache.commons.codec.digest.DigestUtils;

public class Encryptor {
    private static final String AES_PASS = "0ca763dc6b05b5230e44beb6b90e346440204b6d334b09623eafd3fcfbad6a302faca28b0994872e3fd782e7353026684b7ac9385662144e0ed1e2a8e3e14fab79059929681e3794eb97271328ecccda6dbfb3a7991ea1324615cf5908fabdf6"; // Hashed into an AES key later
    private SecretKeySpec keyObj;
    private Cipher cipher;
    private IvParameterSpec ivObj;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();


    public Encryptor() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        // A constant IV, since CBC requires an IV but we don't really need one

        String ivValue = new StringBuilder("astring").reverse().toString() + new StringBuilder("0ca763dc6b05b5230e44beb6b90e346440204b6d334b09623eafd3fcfbad6a302faca28b0994872e3fd782e7353026684b7ac9385662144e0ed1e2a8e3e14fab").reverse();
        System.out.println("ivValue => "+ivValue);
        try {
            byte[] ivkey = ivValue.getBytes("UTF-8");
            MessageDigest shaIv = MessageDigest.getInstance("SHA-256");
            ivkey = shaIv.digest(ivkey);
            ivkey = Arrays.copyOf(ivkey, 16);
            System.out.println("IV => "+bytesToHex(ivkey));
            this.ivObj = new IvParameterSpec(ivkey);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Create an SHA-256 256-bit hash of the key
        byte[] key = AES_PASS.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 32); // Use only first 256 bit
        System.out.println("SEC KEY => "+bytesToHex(key));
        this.keyObj = new SecretKeySpec(key, "AES");

        // Create a Cipher by specifying the following parameters
        //  a. Algorithm name - here it is AES 
        //  b. Mode - here it is CBC mode 
        //  c. Padding - e.g. PKCS7 or PKCS5
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    public String encrypt(String strDataToEncrypt) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
        String strCipherText = new String();

        this.cipher.init(Cipher.ENCRYPT_MODE, this.keyObj, this.ivObj);

        // Encrypt the Data 
        //  a. Declare / Initialize the Data. Here the data is of type String 
        //  b. Convert the Input Text to Bytes 
        //  c. Encrypt the bytes using doFinal method
        byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();

        byte[] byteCipherText = this.cipher.doFinal(byteDataToEncrypt);

        // b64 is done differently on Android
        strCipherText = Base64.encodeBase64String(byteCipherText);

        return strCipherText;
    }

    public String decrypt(String strCipherText) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
        String strDecryptedText = new String();

        // Initialize the Cipher for Encryption
        this.cipher.init(Cipher.DECRYPT_MODE, this.keyObj, this.ivObj);

        // Decode the Base64 text
        byte[] cipherBytes = Base64.decodeBase64(strCipherText);

        // Decrypt the Data
        //  a. Initialize a new instance of Cipher for Decryption (normally don't reuse the same object)
        //     Be sure to obtain the same IV bytes for CBC mode.
        //  b. Decrypt the cipher bytes using doFinal method
        byte[] byteDecryptedText = this.cipher.doFinal(cipherBytes);
        strDecryptedText = new String(byteDecryptedText);

        return strDecryptedText;
    }
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main (String args[]) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException{
        Encryptor aesCipher = new Encryptor();
        try {
            String encText = aesCipher.encrypt("Test");
            System.out.println("enc text => "+encText);
            String plaintext = aesCipher.decrypt("Tn2SzI8dmgCmEvQrzdqLxw==");//("eat6f1uCCXVqJgTNUA8BCqXSA4kG4GhKajXdkyV0TewK+jgDkbQ/lPVaevv4rW3XdSmtVyOKLVJjPw9Akeblrh+ejIv9u48n7PkRKniwfxq/URuPU7lhS/sO5JMiJ7+ufgKFvJapxhSfftCtigtDc8F6Y2lJIPEUeQeQKOVc1noeLqPFggz55hWjWvDtpYh/sG76MwLlWDM7cj+uu6ru3ImmDA7qoM4tJOWBBkfng8u20R1ZcF3gM45TgDLUdL912AE1WO+grGBGjqzTXlK2/jgu3OOsLVI0jndB49K5q3/oKJc7JEoIZb0eZJcuZ80A");
            System.out.println("plain text => "+plaintext);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
