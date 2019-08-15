//MainActivity.java
package com.example.login1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity_old extends Activity {

//URL to get JSON Array
private static String url = "http://demo.vtiger.com/webservice.php?operation=getchallenge&username=admin";
//JSON Node Names
private static final String TAG_RESULT = "result";
private static final String TAG_TOKEN = "token";
String token = null;

EditText userid, accesskey;
Button login;
TextView gettoken;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (android.os.Build.VERSION.SDK_INT > 9) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    this.gettoken = (TextView)findViewById(R.id.lblToken);

    new AsyncTask<Void, Void, Void>() {

        JSONArray result;

        @Override
        protected Void doInBackground(Void... params) {

            // Creating new JSON Parser
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                // Getting JSON Array
                result = json.getJSONArray(TAG_RESULT);
                JSONObject json_result = json.getJSONObject(TAG_RESULT);
                // Storing  JSON item in a Variable
                token = json_result.getString(TAG_TOKEN);
                //Importing TextView

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Set JSON Data in TextView
            gettoken.setText(token);
        }
    }.execute();

    userid = (EditText) findViewById(R.id.txtUserid);
    accesskey = (EditText) findViewById(R.id.txtPassword);

    Button login = (Button) findViewById(R.id.btnLogin);

    login.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            /* This code get the values from edittexts  */
            String useridvalue = userid.getText().toString();
            String accesskeyvalue = accesskey.getText().toString();

            /*To check values what user enters in Edittexts..just show in logcat */ 
            Log.d("useridvalue",useridvalue);
            Log.d("accesskeyvalue",accesskeyvalue);

        String md=md5(accesskeyvalue + token);
        System.out.println(md);
        }

        public String md5(String s) 
            {
            MessageDigest digest;
                try 
                    {
                        digest = MessageDigest.getInstance("MD5");
                        digest.update(s.getBytes(),0,s.length());
                        String hash = new BigInteger(1, digest.digest()).toString(16);
                        return hash;
                    } 
                catch (NoSuchAlgorithmException e) 
                    {
                        e.printStackTrace();
                    }
                return "";
            }
    });

  }
}
