package com.test.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Hex;

public class TrippleDESTest
{
    private Cipher ecipher;
    private Cipher dcipher;
    private String algorithm = "DESede";
    private String transformation = "DESede/CBC/PKCS5Padding";
    private String keyPhrase = "123456789012345678901234"; //your keyphrase 24 bit
    private SecretKey key;
    private IvParameterSpec iv;
    private static TrippleDESTest cryptoUtil;
    private String ENCODING = "UTF-8";

    public static TrippleDESTest getInstance() throws Exception
    {
        if (cryptoUtil == null)
        {
            cryptoUtil = new TrippleDESTest();
        }

        return cryptoUtil;
    }

    private TrippleDESTest() throws Exception
    {
            DESedeKeySpec keySpec = new DESedeKeySpec(keyPhrase.getBytes());
            key = SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec);
            iv = new IvParameterSpec(new byte[8]);
            ecipher = Cipher.getInstance(transformation);
            dcipher = Cipher.getInstance(transformation);
            ecipher.init(Cipher.ENCRYPT_MODE, key, iv);
            dcipher.init(Cipher.DECRYPT_MODE, key, iv);
    }

    public String encrypt(String str) throws Exception
    {
            byte[] utf8 = str.getBytes(ENCODING);    
            byte[] enc = ecipher.doFinal(utf8);                
            System.out.println("number of bites: " + enc.length);    
            return Hex.encodeHexString(enc);
    }

    public String decrypt(String str) throws Exception
    {
            byte[] dec = str.getBytes();
            byte[] utf8 = dcipher.doFinal(dec);    
            return Hex.encodeHexString(utf8);
    }

    public static void main(String[] args) throws Exception
    {
        TrippleDESTest test = TrippleDESTest.getInstance();        
        String originalText = "Abcdefgh";        
        System.out.println("originalText: " + originalText);        
        String cryptText = test.encrypt(originalText);        
        System.out.println("cryptText: " + cryptText);        
        System.out.println("cryptText.length: " + cryptText.length());
        System.out.println("cryptText.getBytes().length: " + cryptText.getBytes().length);        
        System.out.println("decote text: " + test.decrypt(cryptText));

    }
}// end class TrippleDESTest
