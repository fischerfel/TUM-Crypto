package com.example.nw_encrypt_decrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.os.Bundle;
import android.app.Activity;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

EditText data;
Button bencrypt;
Button bdecrypt;
TextView encdec;

static byte[] raw = new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
static SecureRandom rnd = new SecureRandom();
static IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    data = (EditText) findViewById(R.id.editText1);
    String plaintext = data.getText().toString();
    bencrypt =(Button) findViewById(R.id.button1);
    bdecrypt =(Button) findViewById(R.id.button2);
    encdec  =(TextView) findViewById(R.id.textView1);

    //"1234567890123456"
    final String mencrypt = encrypt(plaintext);
    //System.out.println("decrypted value:" + (decrypt("ThisIsASecretKey", mencrypt)));



    bencrypt.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub


            encdec.setText("encrypted value : "+mencrypt);
        }
    });


    bdecrypt.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            encdec.setText("decrypted value : " + (decrypt("ThisIsASecretKey", mencrypt)));
        }
    });
}


public static String encrypt(String value) {
    try {

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        //System.out.println("encrypted string:" + Base64.encodeBase64String(encrypted));
        //encdec.setText("encrypted string:" + Base64.encodeBase64String(encrypted));
        return Base64.encodeToString(encrypted,Base64.DEFAULT);


        //return Base64.encodeBase64String(encrypted);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}

public static String decrypt(String key, String encrypted) {
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,iv);
        //byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

        byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
        //String original = cipher.doFinal(Base64.decode(encrypted, 0));

        return new String(original);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}

}
