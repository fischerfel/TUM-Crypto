package com.andc.billing.pdc.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PasswordCrypto {

    private static final String password = "#$^&*!@!$";
    private static String initializationVector = "@1B2c3D4e5F6g7H8";
    private static String salt = "R@j@}{BAe";
    private static int pswdIterations = 2;
    private static int keySize = 128;
    private static final Log log = LogFactory.getLog(PasswordCrypto.class);

    public static String encrypt(String plainText) throws 
        NoSuchAlgorithmException, 
        InvalidKeySpecException, 
        NoSuchPaddingException, 
        InvalidParameterSpecException, 
        IllegalBlockSizeException, 
        BadPaddingException, 
        UnsupportedEncodingException, 
        InvalidKeyException, 
        InvalidAlgorithmParameterException, java.security.InvalidKeyException, NoSuchProviderException 
    {   
        byte[] saltBytes = salt.getBytes("ASCII");//"UTF-8");
        byte[] ivBytes = initializationVector.getBytes("ASCII");//"UTF-8");

        // Derive the key, given password and salt.
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");//PBEWithMD5AndDES");
        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(), 
                saltBytes, 
                pswdIterations, 
                keySize
        );

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");


        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //Cipher.getInstance("AES/CBC/PKCSPadding"
        cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivBytes));

        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("ASCII"));//UTF-8"));
        String str=new org.apache.commons.codec.binary.Base64().encodeAsString(encryptedTextBytes);
        log.info(str);
        return str;
    }
}
