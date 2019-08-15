package com.example.encryptographytest;

import java.security.Security;
import java.security.spec.KeySpec;    
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String key = "1234567891234567";
      String data = "example";
     Log.d("CRYPTO-TEST", ""+ decrypt(encrypt(data, key), key));
      Log.d("CRYPTO-TEST", ""+encrypt(data, key));  
    }

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

       // return new String(Base64.encodeBase64(crypted));
    }

    public static String decrypt(String input, String key){
        byte[] output = null;
        try{
          SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
          Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
          cipher.init(Cipher.DECRYPT_MODE, skey);
          output = cipher.doFinal( Base64.decode(input, Base64.DEFAULT));
        }catch(Exception e){
          System.out.println(e.toString());
        }
        return new String(output);
    }     
}
