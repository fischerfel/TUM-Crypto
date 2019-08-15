package com.example.phonefinder2;

import java.math.BigInteger;   
import java.security.MessageDigest;   
import java.security.NoSuchAlgorithmException;   

import android.app.Activity;   
import android.content.SharedPreferences.Editor;   
import android.os.Bundle;   
import android.util.Log;   
import android.view.View;   
import android.view.View.OnClickListener;   
import android.widget.Button;   
import android.widget.EditText;   
import android.widget.TextView;   

public class PhoneFinder extends Activity {   
    private EditText edit01;   
    private EditText edit02;   
    private Button button_ok;   
    private TextView textview;   
    static final String PASSWORD_PREF_KEY = "passwd";   
    /** Called when the activity is first created. */   
    @Override   
    public void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.main);   

        edit01 = (EditText)findViewById(R.id.password);   
        edit02 = (EditText)findViewById(R.id.password_confirm);   
        textview = (TextView)findViewById(R.id.text1);   
        button_ok = (Button)findViewById(R.id.ok);   
        button_ok.setOnClickListener(listener);   
        /*SharedPreferences setting = getSharedPreferences(PASSWORD_PREF_KEY, 0);  
        String t1 = setting.getString("PASSWORD", null);  
        edit01.setText(t1);  
        edit02.setText(t1);*/   
    }   

    OnClickListener listener = new OnClickListener() {   
        public void onClick(View v) {   
            String p1 = edit01.getText().toString();   
            String p2 = edit02.getText().toString();   
            if(p1.equals(p2)) {   
                if(p1.length() >= 6 && p2.length() >= 6) {   
                    Editor edit = getSharedPreferences(PASSWORD_PREF_KEY, MODE_PRIVATE).edit();   
                    String md5hash = getMd5Hash(p1);   
                    edit.putString("PASSWORD", md5hash);   
                    edit.commit();   
                    textview.setText("password updated");   
                } else {   
                    textview.setText("password must be at least 6 characters");   
                }   
            } else {   
                edit01.setText("");   
                edit02.setText("");   
                textview.setText("password do not match");   
            }   
        }   
    };   

    public static String getMd5Hash(String input) {    
        try {    
            MessageDigest md = MessageDigest.getInstance("MD5");    
            byte[] messageDigest = md.digest(input.getBytes());    
            BigInteger number = new BigInteger(1,messageDigest);    
            String md5 = number.toString(16);    

            while (md5.length() < 32)    
                md5 = "0" + md5;    
                return md5;    
        } catch(NoSuchAlgorithmException e) {    
            Log.e("MD5", e.getMessage());    
            return null;    
        }    
    }    
}   
