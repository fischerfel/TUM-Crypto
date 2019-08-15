Here's my MainActivity Class
package com.infamuspips.cess;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Encryption";
    EditText editText,username,password;
    Button button;
    TextView cancel,forgot_password;
    static String Username = null;
    static String PassWord = null;
    static String GetUsername = null;
    static String GetPassword = null;
    SecretKey secretKey;
    String cipherText, decryptedText;
    KeyGenerator keyGen;
    Cipher aesCipher;
    FileOutputStream fos;
    byte[] byteDataToEncrypt, byteCipherText, byteDecryptedText;

    TextWatcher textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            String User = username.getText().toString();
            String Pass = password.getText().toString();


            if (!User.isEmpty() && !Pass.isEmpty()) {
                button.setEnabled(true);

            } else {
                button.setEnabled(false);
            }

        }
        @Override
        public void afterTextChanged(Editable s) {

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        editText = (EditText) findViewById(R.id.editText);
        username.addTextChangedListener(textwatcher);
        password.addTextChangedListener(textwatcher);
        button = (Button) findViewById(R.id.logIn);
        cancel = (TextView) findViewById(R.id.LoginCancel);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        //Set Clickable attributes
        cancel.setClickable(true);
        button.setEnabled(false);
        editText.setEnabled(false);
        //Call Operation Methods
        username.requestFocus();
        //Cancel();
        //Forgot_Password();
        Log_In();


    }

    private void Cancel(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),registration.class);
                startActivity(i);
            }
        });

    }
    private void Forgot_Password(){
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
    class PerformBackGround extends AsyncTask<Void, Void, String>{
        private ProgressDialog mDialog;

        public PerformBackGround() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please wait");
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setProgress(0);
            mDialog.setMax(10);
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            try{

                String link = "http://*************************";

                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String result = bufferedReader.readLine();
                return result;

            }catch (Exception e){
                return "Exception" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String jsonStr = result;
            String response;


            if (jsonStr != null){
                try{


                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String User = jsonObj.get("name").toString();
                    String Password = jsonObj.get("surname").toString();

                    mDialog.cancel();
                    if (User.equals(Username) && Password.equals(PassWord)){
                        //String name = jsonObj.get("name").toString();
                        //String Surname = jsonObj.get("surname").toString();
                        response = jsonObj.get("surname").toString();
                        getResponse(response);
                        Intent i = new Intent(getApplicationContext(),Maindrawer.class);
                        startActivity(i);

                    }else{
                        response = "invalid";
                        getResponse(response);
                    }
                }catch (Exception e){
                    mDialog.cancel();
                    e.printStackTrace();
                    response =  "unable";
                    getResponse(response);
                }
            }else{
                mDialog.cancel();
                response = "unconnect";
                getResponse(response);

            }
        }

    }
    private void Log_In(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PerformBackGround bg = new PerformBackGround();
                username = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);
                String user = username.getText().toString();
                String pass = password.getText().toString();
                Username = user;
                PassWord = pass;
                //addmodules addm =new addmodules();
                //addm.execute();
                Toast.makeText(MainActivity.this, md5(username.getText().toString()), Toast.LENGTH_SHORT).show();


                Intent i = new Intent(getApplicationContext(),Maindrawer.class);
                startActivity(i);

                username.getText().clear();
                password.getText().clear();

            }
        });
    }
    public String md5(String text) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(text.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
