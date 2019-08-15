    package com.example.mysqltest;
                         import android.app.Activity;
                         import android.app.ProgressDialog;
                         import android.content.Intent;
                         import android.os.AsyncTask;
                         import android.os.Bundle;
                         import android.view.View;
                         import android.view.View.OnClickListener;
                         import android.widget.Button;
                         import android.widget.EditText;
                         import android.widget.Toast;
                         import org.apache.http.NameValuePair;
                         import org.apache.http.message.BasicNameValuePair;
                         import org.json.JSONException;
                         import org.json.JSONObject;

                         import java.util.ArrayList;
                         import java.util.List;

                         public class Login extends Activity implements OnClickListener{

    private EditText user, pass;
    private Button mSubmit, mRegister;

     // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script location:




    //testing from a real server:
                         private static final String LOGIN_URL =           "http://www.eshteghel.comlu.com/dbservice/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //setup input fields
        user = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);


        //setup buttons
        mSubmit = (Button)findViewById(R.id.login);
        mRegister = (Button)findViewById(R.id.register);

        //register listeners
        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.login:
                new AttemptLogin().execute();
            break;
        case R.id.register:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
            break;

        default:
            break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();

            try {

    /*            try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(password.getBytes(),0,password.length());
                    password = new BigInteger(1,md5.digest()).toString(16);
                    //System.out.println("Signature: "+signature);

                } catch (final NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }*/

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                //Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);

                // check your log for json response
                //Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    //Log.d("Login Successful!", json.toString());

                    Intent i = new Intent(Login.this, ReadComments.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    //Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

            /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}
