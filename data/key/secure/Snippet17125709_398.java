package karp.generalutil.common;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Encryptor {

/**
 * @param args
 */

static Cipher eCipher,dCipher;

public Encryptor()
{
    try {
        eCipher=Cipher.getInstance("AES");
        dCipher=Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


}

/// for test
public static void main (String args[])
{
try {
    KeyGenerator aes;


    aes = KeyGenerator.getInstance("AES");

    aes.init(128);

    SecretKey key = aes.generateKey();






} catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}

}


public static String encrypt(String clearText,String keyString) throws NoSuchAlgorithmException, 
                                                       NoSuchPaddingException, InvalidKeyException,
                                                        IllegalBlockSizeException, BadPaddingException
{


    SecretKey key=loadKey(keyString);
    String encryptedText;

    eCipher.init(Cipher.ENCRYPT_MODE,key );
    encryptedText=new String(eCipher.doFinal(clearText.getBytes()));





    return encryptedText;
}

public static String decrypt(String encryptedText,String keyString) throws NoSuchAlgorithmException, 
                                                           NoSuchPaddingException, InvalidKeyException,
                                                           IllegalBlockSizeException, BadPaddingException
{

    SecretKey key=loadKey(keyString);
    String clearText;
    Cipher dCipher=Cipher.getInstance("AES");
    dCipher.init(Cipher.DECRYPT_MODE,key);
    clearText=new String(dCipher.doFinal(encryptedText.getBytes()));





    return clearText;


}


public static byte[] encrypt(byte [] clearByteArray,String keyString)throws NoSuchAlgorithmException, 
                                                            NoSuchPaddingException, InvalidKeyException,
                                                            IllegalBlockSizeException, BadPaddingException
{

    SecretKey key=loadKey(keyString);
    byte[] encryptedByteArray;
    Cipher eCipher=Cipher.getInstance("AES");
    eCipher.init(Cipher.ENCRYPT_MODE,key );
    encryptedByteArray=eCipher.doFinal(clearByteArray);

    return encryptedByteArray;
}


public static byte[] decrypt(byte [] encryptedByteArray,String keyString)throws NoSuchAlgorithmException, 
                                                            NoSuchPaddingException, InvalidKeyException,
                                                            IllegalBlockSizeException, BadPaddingException
{
    SecretKey key=loadKey(keyString);
    byte[] clearByteArray;

    Cipher dCipher=Cipher.getInstance("AES");
    dCipher.init(Cipher.DECRYPT_MODE,key );
    clearByteArray=dCipher.doFinal(encryptedByteArray);
    return clearByteArray;
}

public static SecretKey loadKey(String  keyString)  {

        byte[] encoded = keyString.getBytes();

        SecretKey key = new SecretKeySpec(encoded, "AES");
        return key;
    }

}
