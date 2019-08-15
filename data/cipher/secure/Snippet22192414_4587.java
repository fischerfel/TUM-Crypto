package com.exam.encrypttest;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity {
TextView tv1;
Button but1;
EditText edit1;
String type = "AES";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    edit1 = (EditText) findViewById(R.id.edit01);
    edit1.setText("ABCDEFGH");
    tv1 = (TextView) findViewById(R.id.text01);
    but1 = (Button) findViewById(R.id.button01);
    but1.setOnClickListener(new ButtonClick());
}

class ButtonClick implements View.OnClickListener {
    public void onClick(View v) {
        int sel = v.getId();
        switch (sel) {
        case R.id.button01:
            Encryption(edit1.getText().toString(), type);
            break;
        }
    }

    private void Encryption(String text, String key) {
        long time1 = System.currentTimeMillis();
        Cipher cipher;
        SecretKeySpec skeySpec;
        KeyGenerator kgen;

        try {
            kgen = KeyGenerator.getInstance(key);
            SecretKey skey = kgen.generateKey();
            kgen.init(128);
            byte[] raw = skey.getEncoded();

            skeySpec = new SecretKeySpec(raw, key);
            cipher = Cipher.getInstance(key);

            byte[] encrypted = Encrypt(text, skeySpec, cipher);
            String sendtext = Base64.encode(encrypted);

            long time2 = System.currentTimeMillis();


            byte[] abc = Base64.decode(sendtext);
            byte[] decrypted = Decrypt(abc, skeySpec, cipher);
            long time3 = System.currentTimeMillis();            
            tv1.setText("Encrypt Time(ms) : "+(time2-time1));



        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] Encrypt(String data, SecretKeySpec keySpec,
            Cipher cipher) throws Exception, ArrayIndexOutOfBoundsException {

        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return encrypted;
    }

    public byte[] Decrypt(byte[] encrypted_data,
            SecretKeySpec keySpec, Cipher cipher) throws Exception,
            ArrayIndexOutOfBoundsException {
        cipher.init(Cipher.DECRYPT_MODE, keySpec); 
        byte[] decrypted = cipher.doFinal(encrypted_data);
        return decrypted;
    }
}

}
