package com.kezinking.nupur.kezinking;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.R.attr.password;

/**
 * Created by Nupur on 8/2/2016.
 */

public class LoginActivity extends AppCompatActivity {
    public static final String MY_JSON = "MY_JSON";
    private static final String JSON_URL = "http://kezinking.com/AndroidLogin";

    EditText input_email, input_password;
    Button btn_login, btnContactUs, btnWorkhr, btnDeltime, btnMoneyBack;
    TextView link_signup, textViewJSON, txtcheck,value;
    private static Boolean flag = false;
   public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String emailkey = "emailKey";
    public static final String UserId = "user_id";
    SharedPreferences sharedpreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (Button) findViewById(R.id.btn_login);
        link_signup = (TextView) findViewById(R.id.link_signup);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        textViewJSON = (TextView) findViewById(R.id.textViewJSON);
        // value=(TextView)findViewById(R.id.value);
        txtcheck = (TextView) findViewById(R.id.txtcheck);
        value = (TextView) findViewById(R.id.value);
//shared preference folder
  sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //onClick Event on login Button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                LoginActivity.this.getIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        } else {
            getJSON(JSON_URL);
        }
    }

    public void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    //String result=null;
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                textViewJSON.setText(s);
                String jsonString = textViewJSON.getText().toString();

                String email = input_email.getText().toString();
                String password = input_password.getText().toString();
              /**  md5(password); // Enrcypting the given password **/




                // final String myResult = (email) + (password);
              //  String value = (String.valueOf(email + password));

               // System.out.println(value);
                // String value=(input_email.getText().toString()+input_password.getText().toString());
                //String input_email = "";
                //String input_password = "";
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray result = jsonObject.getJSONArray("result");
                    //System.out.println("result " + result);
                    //iterate through json array and check if id is same with your search

                    //System.out.println("length " + result.length());
                    for (int i = 0; i < result.length(); i++) {
                       // System.out.println(i);
                        JSONObject item = result.getJSONObject(i);
                        String Email = item.getString("email");
                        String Password = item.getString("password");
                        //System.out.println("Email from json "+ Email);
                        //System.out.println("Password from json " + Password);
                        if (Email.equals(email) && Password.equals(password)) {
                            //System.out.println("Email from json"+ Email);
                           // System.out.println("Password from json" + Password);
                         String username=item.getString("username");
                            String userid=item.getString("user_id");

                            String phone_number=item.getString("con_no");
                            String emailSaved = item.getString("email");
                       //  System.out.println("Email from json "+ Email);
                          //  System.out.println("Password from json " + Password);
                         //   System.out.println("username from json " + username);
                          //  System.out.println("userid from json " + userid);
                        //    System.out.println("phone_number from json " + phone_number);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
// Store data in shared preference
                            editor.putString(Name, username);
                            editor.putString(Phone, phone_number);
                            editor.putString(UserId,userid);
                            editor.putString(emailkey, emailSaved);
                            editor.commit();

                            flag=true;
// Retrieve data from shared Preference folder
                            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

                            String show_name = prefs.getString("Name", username); // getting data-name from shared preference
                            String show_email=prefs.getString("emailkey", emailSaved); // retrieving data from shared preference folder
// Checking the values are coming correct from shared preference folder or not
                            System.out.println("Shared_preference-email " + show_email);
                            System.out.println("Shared_preference-username " + show_name);
                           // value.setText(restoredText);

                            onLoginSuccess();


                            break;
                        }
                    }
                    if(!flag){
                        onLoginFailed();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//here you can show the result
                Log.v(this.getClass().getSimpleName(), "email = " + email);
                Log.v(this.getClass().getSimpleName(), "password = " + password);


            }
        }


        GetJSON gj = new GetJSON();
        gj.execute(url);

    }
 /**   private String md5(String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(password.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e)
        { e.printStackTrace(); }
        System.out.print(password);
       return password;


    } **/


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btn_login.setEnabled(true);
        if(sharedpreferences!=null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.getIntent();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Shared preference is null", Toast.LENGTH_LONG).show();

        }




        // finish();
    }


    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        // btn_login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }

}
