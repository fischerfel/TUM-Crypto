package com.MD5Check;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.os.Bundle;

public class MD5Check extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSignature();
    }

    public void getSignature()
    {
        try {
               String s = "aditi9970";
               MessageDigest md5 = MessageDigest.getInstance("MD5");
               md5.update(s.getBytes(),0,s.length());
               String signature = new BigInteger(1,md5.digest()).toString(16);
               System.out.println("Signature: "+signature);

            } catch (final NoSuchAlgorithmException e) {
               e.printStackTrace();
            }
    }
} 
