package Algorithms;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MyDES {
    public static String encrypt(String pass,String plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        byte[] key = pass.getBytes("UTF-8"); //get byte arrays of the given password
        MessageDigest sha = MessageDigest.getInstance("SHA-1"); //get SHA-1 hashing instance
        key=sha.digest(key); //has the given password
        key=Arrays.copyOf(key,24);//take the first 16 bytes as the key for DES encryption

        SecretKeySpec sks = new SecretKeySpec(key, "DESede");//key spec for 3-DES
        Cipher c = Cipher.getInstance("DESede");//get an instance of 3DES
        c.init(Cipher.ENCRYPT_MODE,sks); //initialize 3DES to encrypt mode with given parameters
        byte[] cipherTextBytes = c.doFinal(plainText.getBytes()); //encrypt

        System.out.println("key used: "+new String(key)+" cipher generated "+new String(cipherTextBytes));
        StringBuffer cipherText= new StringBuffer();
        for(int i=0;i<cipherTextBytes.length;i++)
        {
            cipherText.append(Integer.toHexString(cipherTextBytes[i]));
        }

        System.out.println("Final Cipher returned: "+cipherText.toString());
        return cipherText.toString();
    }

    public static String decrypt(String pass,String cipherText) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        System.out.println("Initially in decryption-> pass:"+pass+" cipher: "+cipherText);
        byte[] byteArray = new byte[cipherText.length() / 2];
        int j=0;
        for(int k=0;k<cipherText.length()-1;k+=2)
        {
            String o= cipherText.substring(k,k+2);
            int dec = Integer.parseInt(o,16);
            byteArray[j++] = (byte)dec;
        }

        String plainText="";
        byte[] key = pass.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key=sha.digest(key);
        key=Arrays.copyOf(key,24);
        System.out.println("\nkey obtained: "+new String(key)+"\n Later cipher text:-> "+new String(byteArray));

        SecretKeySpec sks = new SecretKeySpec(key, "DESede");
        Cipher c = Cipher.getInstance("DESede");
        c.init(Cipher.DECRYPT_MODE,sks);
        plainText = new String(c.doFinal(byteArray));
        return plainText;
    }

}
