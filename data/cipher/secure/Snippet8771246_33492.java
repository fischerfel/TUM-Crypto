package com.myclass.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.regex.Pattern;
import javax.crypto.*;
import javax.crypto.spec.*;    

   public class AES {
       private static String provider = "AES/CTR/NoPadding";
       private static String providerkey = "AES";
       private static int size = 128;
       private SecretKeySpec key;
       private Cipher cipher;
       private byte[] ivBytes = new byte[size/8];
       private IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);


       public AES() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException{
           Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
           KeyGenerator kgen = KeyGenerator.getInstance(providerkey);
           kgen.init(size); // 192 and 256 bits may not be available
           SecretKey skey = kgen.generateKey();
           byte[] raw = skey.getEncoded();
           key = new SecretKeySpec(raw, providerkey);
           cipher = Cipher.getInstance(provider);
           for(int x = 0; x < (size/8); x++)
               ivBytes[x] = 00;
           ivSpec = new IvParameterSpec(ivBytes);
       }

       public void setKey(String keyText){
           byte[] bText = new byte[size/8];
           bText = keyText.getBytes();
           key = new SecretKeySpec(bText, providerkey);
       }

       public void setIV(String ivText){
           setIV(ivText.getBytes());
       }

       public void setIV(byte[] ivByte){
           byte[] bText = new byte[size/8];
           bText = ivByte;
           ivBytes = bText;
           ivSpec = new IvParameterSpec(ivBytes);
       }

       public String encrypt(String message) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException{
           cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
           byte[] encrypted = cipher.doFinal(message.getBytes());
           return byteArrayToHexString(encrypted);
       }
       public String decrypt(String hexCiphertext) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException{
           cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
           byte[] dec = hexStringToByteArray(hexCiphertext);
           byte[] decrypted = cipher.doFinal(dec);
           return new String(decrypted);
       }

       private static String byteArrayToHexString( byte [] raw ) {
            String hex = "0x";
            String s = new String(raw);
            for(int x = 0; x < s.length(); x++){
                char[] t = s.substring(x, x + 1).toCharArray();
                hex += Integer.toHexString((int) t[0]).toUpperCase();
            }
            return hex;
       }

       private static byte[] hexStringToByteArray(String hex) {
            Pattern replace = Pattern.compile("^0x");
            String s = replace.matcher(hex).replaceAll("");

            byte[] b = new byte[s.length() / 2];
            for (int i = 0; i < b.length; i++){
              int index = i * 2;
              int v = Integer.parseInt(s.substring(index, index + 2), 16);
              b[i] = (byte)v;
            }
            return b;
       }


   }
