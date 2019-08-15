package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;



import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class ByteArrayClass {

    private static byte[] ivBytes;
    public static byte[] keyValue = new byte[32];
    public static String cryptText = ""; 
    public static String encryptText = ""; 

    public static void main(String[] args){
        //
        System.out.println("Start");
        String str = "MySuperSecretKey"; //AES allows 128, 192 or 256 bit key length. That is 16, 24 or 32 byte
        try {
            keyValue = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String clearText = "123456789012345623";
        cryptText = encrypt(clearText);
        encryptText = decrypt(cryptText);
        System.out.println("Clear text: " + clearText);
        System.out.println("Crypted text: " + cryptText);
        System.out.println("Encrypted text: " + encryptText);
        System.out.println("Finish");
    }

     public static String encrypt(String plainText){   

            System.out.println("plainText :" + plainText + " : " + plainText.length());
            Key key=null;;
            try {
                key = generateKey();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                System.out.println("key = generateKey()");
                e1.printStackTrace();
            }
            Cipher cipher=null;
            try {
                cipher = Cipher.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            byte[] clearTextBytes = new byte[32];
            try {
                clearTextBytes = plainText.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println("clearTextBytes : " + clearTextBytes + " : " + clearTextBytes.length);
            clearTextBytes = checkTextLenght(clearTextBytes);
            System.out.println("checkTextLenght.clearTextBytes : " + clearTextBytes.length);
            byte[] encryptedTextBytes;
            try {
                encryptedTextBytes = cipher.doFinal();
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                encryptedTextBytes=null;
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                encryptedTextBytes=null;
            }
            //@Override
            String encryptedValue = new BASE64Encoder().encode(encryptedTextBytes);
            System.out.println("encryptedValue : " + encryptedValue.toString() + " : " + encryptedValue.toString().length());
            System.out.println("encryptedTextBytes : " + encryptedTextBytes.toString() + " : " + encryptedTextBytes.length);
            return encryptedValue.toString();
        }

     public static String decrypt(String encryptedValue){ 
        System.out.println("DECRYPT");
        System.out.println("encryptedValue : " + encryptedValue + " : " + encryptedValue.length());
        Key key=null;;
        try {
            key = generateKey();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("key = generateKey()");
            e.printStackTrace();
        } 
        Cipher c=null;;
        try {
            c = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        try {
            //c.init(Cipher.DECRYPT_MODE, key,ivspec);
            c.init(Cipher.DECRYPT_MODE,key);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            System.out.println("c.init(Cipher.DECRYPT_MODE, key);");
            e.printStackTrace();
        } 

        String decordedValue;
        byte[] byteDecod;
        byte[] inBytes;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            inBytes = decoder.decodeBuffer(encryptedValue);
            System.out.println("inBytes (decode from BASE64) : " + inBytes + " : size : " + inBytes.length);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            decordedValue=null;
            inBytes=null;
            e1.printStackTrace();
        } 
        byte[] decValue;
        byte[] decryptByte;
        String returnString;

        try {
            decValue = c.doFinal(inBytes);
            System.out.println("---------------------------------");
            System.out.println(decValue + " : size : " + decValue.length);
            System.out.println("---------------------------------");
            System.out.println(decValue.toString() + " : convert to string : " + new String(decValue, Charset.forName("UTF-8")) + " |");
            System.out.println("---------------------------------");
            int i=0;

        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            decValue=null;
            returnString="";
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            decValue=null;
            returnString="";
            e.printStackTrace();
        }
         String decryptedValue = new String(decValue); 
         return decryptedValue; 
         }

     public static String generateSalt() {
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[26];
            random.nextBytes(bytes);
            String s = new String(bytes);
            return s;
        }

     private static Key generateKey() throws Exception 
     {
             Key key = new SecretKeySpec(keyValue, "AES");
             return key;
     }

     private static byte[] checkTextLenght (byte[] clearTextBytes){
         //
         int size = (clearTextBytes.length / 16 ) + ( (clearTextBytes.length % 16 == 0) ? 0 : 1 ) ;
            int newLength = size * 16;
            int lengthOfPad = newLength - clearTextBytes.length;

            // set the pad character according to PKCS5Padding algorithm
            byte padByte = 0;
            switch (lengthOfPad) {
                case 16 : padByte = (byte) 0x10; break;
                case 15 : padByte = (byte) 0x0f; break;
                case 14 : padByte = (byte) 0x0e; break;
                case 13 : padByte = (byte) 0x0d; break;
                case 12 : padByte = (byte) 0x0c; break;
                case 11 : padByte = (byte) 0x0b; break;
                case 10 : padByte = (byte) 0x0a; break;
                case 9 : padByte = (byte) 0x09; break;
                case 8 : padByte = (byte) 0x08; break;
                case 7 : padByte = (byte) 0x07; break;
                case 6 : padByte = (byte) 0x06; break;
                case 5 : padByte = (byte) 0x05; break;
                case 4 : padByte = (byte) 0x04; break;         
                case 3 : padByte = (byte) 0x03; break;        
                case 2 : padByte = (byte) 0x02; break;      
                case 1 : padByte = (byte) 0x01; break;               
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(newLength);
            byteBuffer.put(clearTextBytes);
            byte[] paddedUp = new byte[size * 16];

            for (int j=0;j<clearTextBytes.length;j++) {
                paddedUp[j]=byteBuffer.get(j);
            }
            for (int k=clearTextBytes.length;k<paddedUp.length;k++) {
                paddedUp[k]=padByte;
            }        
            //return paddedUp;
         return paddedUp;
     }

}
