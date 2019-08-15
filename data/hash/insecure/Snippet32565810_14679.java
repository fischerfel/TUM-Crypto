package com.example.kalkulator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = "MD5";
    //private TextView textView_hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        final EditText editText1 = (EditText)findViewById(R.id.editText1);
        final TextView textView3 = (TextView)findViewById(R.id.textView3);

        final String EditText1 = editText1.getText().toString();

        button1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hitungMD5(EditText1);
            }

            private void hitungMD5(String editText1) {
                // TODO Auto-generated method stub
                try
                {
                    MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                    digest.update(editText1.getBytes());
                    byte messageDigest[] = digest.digest();

                    StringBuffer MD5Hash = new StringBuffer();
                    for(int i = 0; i < messageDigest.length; i++)
                    {
                        String h = Integer.toHexString(0xFF & messageDigest[i]);
                        while(h.length() < 2)
                            h = "0" + h;
                        MD5Hash.append(h);
                    }

                    textView3.setText("Hash Anda: " + MD5Hash);
                }
                catch(NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }
            }


        });

        button2.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editText1.setText("");
                textView3.setText("");
            }
        });

    }
