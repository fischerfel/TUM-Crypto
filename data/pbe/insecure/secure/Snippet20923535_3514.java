package com.devleb.testforencdemo;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    String plaintTxt1;
    String pass1;

    String plaintxt;
    String strPass;
    String cypherTxt = "";
    EditText strTxt;
    EditText editStrPass;

    TextView encryptTxt;

    Button btn;

    int iterations = 200;
    byte[] salt = { (byte) 0x11, (byte) 0x9B, (byte) 0xC6, (byte) 0xFE,
            (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x77 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        strTxt = (EditText) findViewById(R.id.editTxtSTR);
        plaintxt = strTxt.getText().toString();

        encryptTxt = (TextView) findViewById(R.id.encryptSTR);
        editStrPass = (EditText) findViewById(R.id.editTxtPass);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Encrypt(plaintTxt1, pass1, salt);
            }
        });

    }

    public static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    public void Encrypt(String pltxt, String pass, byte[] salte) {
        strPass = editStrPass.getText().toString();
        Toast.makeText(this, strPass, Toast.LENGTH_SHORT).show();
        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(pass.toCharArray());
            Cipher cipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
            Log.e("SecreteKey", pbeKey.toString());
            PBEParameterSpec pbeSpec = new PBEParameterSpec(salte, iterations);
            Log.e("SecreteKey", pbeSpec.toString());
            cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeSpec);
            byte[] cipherText = cipher.doFinal(pltxt.getBytes("UTF-8"));
            cypherTxt = String.format("%s%s%s", toBase64(salt), "]",
                    toBase64(cipherText));
            encryptTxt.setText(cypherTxt);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
