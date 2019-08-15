import java.io.*;
import java.math.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class RCC4 {
    public RCC4(){} 

    public static void main(String[] args)throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException{
        String test = "testisperfect";
        System.out.println(RCC4.keyGet());
        byte b[] = RCC4.keyGet().getBytes();
        byte plain[] = test.getBytes();
        **byte c[] = RCC4.encrypt(plain,b);**
        **byte p[] = RCC4.decrypt(c,b);**

        **System.out.println(new String(c)) ;
        System.out.println(new String(p));**

    }
    public static byte[] encrypt(byte[] plaintext,byte[] keyBytes)
    {
        byte[] e = null;
        try
        {
            Key key = new SecretKeySpec(keyBytes,"RC4");
            Cipher enCipher = Cipher.getInstance("RC4");
            **enCipher.init(Cipher.ENCRYPT_MODE ,key);**
            e = enCipher.doFinal(plaintext);           
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return e;
    }
    public static byte[] decrypt(byte[] ciphertext,byte[] keyBytes)
    {
        byte de[] = null;
        try
        {
           Key key = new SecretKeySpec(keyBytes,"RC4");
            Cipher deCipher = Cipher.getInstance("RC4");
           **deCipher.init(Cipher.DECRYPT_MODE, key);**
            de = deCipher.doFinal(ciphertext);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } 
        return de;

    }

    public static Key getKey()
    {
        Key key = null;
        try
        {
            SecureRandom sr = new SecureRandom();
            KeyGenerator kg = KeyGenerator.getInstance("RC4");
            kg.init(128,sr);
            key = kg.generateKey(); 
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return key;
    }
    public static String keyGet()
    {
        Key k = RCC4.getKey();
        byte[] b = k.getEncoded();
    BigInteger big = new BigInteger(b);
        String s = big.toString();
        return s;
    }

    }
