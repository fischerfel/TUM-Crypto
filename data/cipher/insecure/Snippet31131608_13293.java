package com.myApp.security;

import  android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Security {
    public static String encrypt(String input, String key){
      byte[] crypted = null;
      try{
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
          Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
          cipher.init(Cipher.ENCRYPT_MODE, skey);
          crypted = cipher.doFinal(input.getBytes());
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return new String(Base64.encode(crypted, Base64.DEFAULT));
    }

    public static String decrypt(String input, String key){
        byte[] output = null;
        try{
          SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
          Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
          cipher.init(Cipher.DECRYPT_MODE, skey);
          //output = cipher.doFinal(Base64.decodeBase64(input));
          output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));
        }catch(Exception e){
          System.out.println(e.toString());
        }
        return new String(output);
    }   
}
