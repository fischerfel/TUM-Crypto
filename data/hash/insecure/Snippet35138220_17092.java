package com.example.root.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.security.MessageDigest;
import java.io.InputStream;
import java.io.BufferedReader;
import java.security.NoSuchAlgorithmException;
import android.content.res.AssetManager;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button sub;
    EditText mEdit1;
    TextView txtView;
    InputStream in;
    BufferedReader reader;
    String line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

             sub = (Button) findViewById(R.id.button);
             mEdit1 = (EditText) findViewById(R.id.editText);
             txtView=(TextView)findViewById(R.id.textView2);

         try{
             in = this.getAssets().open("file.txt");
             reader = new BufferedReader(new InputStreamReader(in));
            }

          catch(IOException e)
            {
             e.printStackTrace();
            }


        sub.setOnClickListener(new View.OnClickListener() {

            String check1;
            public void onClick(View v) {

                      //"**user enters MD5 hash**" 
                      String message = mEdit1.getText().toString();
               try {

                    // **" reading line by line and comparing hashes "**
                   do {
                       line = reader.readLine();
                       check1=md5(line);
                       if(message.equals(check1))
                            {
                                 txtView.setText("password cracked : "+line);
                                 return;
                            }

                        }while(line!=null);

               }
               catch (IOException e) {
                   e.printStackTrace();

               }

            }
    });
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
