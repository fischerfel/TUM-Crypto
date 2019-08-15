package com.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Coder1 {

    public static int AES_KEY_SIZE = 256 ;
    public static int IV_SIZE = 16 ;
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException 
    {
        String message = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        message =message +message;
        message =message +message;
        KeyGenerator keygen = KeyGenerator.getInstance("AES") ; // Specifying algorithm key will be used for 
        keygen.init(AES_KEY_SIZE) ; // Specifying Key size to be used, Note: This would need JCE Unlimited Strength to be installed explicitly 
        SecretKey aesKey = keygen.generateKey();

        // Generating IV
        byte iv[] = new byte[IV_SIZE];

        SecureRandom secRandom = new SecureRandom() ;
        secRandom.nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5PADDING");
        Cipher decipher = Cipher.getInstance("AES/CTR/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey,new IvParameterSpec(iv));
        decipher.init(Cipher.DECRYPT_MODE, aesKey,new IvParameterSpec(iv));
        byte[] cipherTextInByteArr = cipher.doFinal(message.getBytes());
        byte[] plainTextInByteArr = decipher.doFinal(cipherTextInByteArr);
        System.out.println("Key="+Base64.getEncoder().encodeToString(aesKey.getEncoded())+"|");
        System.out.println("IV="+Base64.getEncoder().encodeToString(iv)+"|");
        System.out.println("Encrypted Text=" + Base64.getEncoder().encodeToString(cipherTextInByteArr)+"|") ;
        System.out.println("Decrypted text=" + new String(plainTextInByteArr));
    }

}
