import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.sunil.R;

public class LoginActivity extends Activity {

    // Facebook APP ID
    public static String APP_ID = "3***********";
    // Instance of Facebook Class
    public Facebook facebook = new Facebook(APP_ID);
    public AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    Person person = new Person();

    // Buttons
    Button btnFbLogin;
    String fb_userid;
    String fb_useremail;
    String fb_username;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loginscreen);
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.authenticationdemo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        // check if you are connected or not
        if (isConnected()) {
            Toast.makeText(getApplicationContext(), "You are connected",
                    Toast.LENGTH_LONG);
        } else {
            Toast.makeText(getApplicationContext(), "You are NOT connected",
                    Toast.LENGTH_LONG);
        }

        // Implementing Login button functionality
        btnFbLogin = (Button) findViewById(R.id.btn_fblogin);
        mAsyncRunner = new AsyncFacebookRunner(facebook);

        /**
         * Login button Click event
         * */
        btnFbLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("Image Button", "button Clicked");
                loginToFacebook();
            }
        });

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * Function to login into facebook
     * */
    @SuppressWarnings("deprecation")
    public void loginToFacebook() {

        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        // Check access token is present or not
        if (access_token != null) {
            facebook.setAccessToken(access_token);
            getProfileInformation();
            Log.d("FB Sessions", "" + facebook.isSessionValid());
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {

            facebook.authorize(this,
                    new String[] { "email", "publish_stream" },
                    new DialogListener() {

                        @Override
                        public void onCancel() {
                            // Function to handle cancel event
                        }

                        @Override
                        public void onComplete(Bundle values) {
                            // Function to handle complete event
                            // Edit Preferences and update facebook acess_token
                            person.setFacebook_access_token1(facebook
                                    .getAccessToken().toString());
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();

                            getProfileInformation();
                        }

                        @Override
                        public void onError(DialogError error) {
                            // Function to handle error
                        }

                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors
                        }

                    });
        }

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            person = new Person();
            return POST(urls[0], person);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            // Toast.makeText(getBaseContext(), result,
            // Toast.LENGTH_LONG).show();
            System.out.println(result);

            try {
                JSONObject parentObject = new JSONObject(result);

                // And then read attributes like
                String message = parentObject.getString("Message");
                String status = parentObject.getString("Status");
                String hash_key = parentObject.getString("hash_key");
                Toast.makeText(
                        getApplicationContext(),
                        "Status: " + status + "  Message: " + message
                                + " Hash_Key" + hash_key, Toast.LENGTH_LONG)
                        .show();
                Intent i = new Intent(getApplicationContext(),
                        Transaction.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("key", hash_key);
                startActivity(i);
                finish();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String POST(String url, Person person) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("email_id", person.getFacebook_emailid1()
                    .toString());
            jsonObject.accumulate("device_type", "Android");
            jsonObject.accumulate("facebook_user_id", person
                    .getFacebook_user_id1().toString());
            jsonObject.accumulate("screen_name", person
                    .getFacebook_user_name1().toString());
            jsonObject.accumulate("facebook_user_name", person
                    .getFacebook_user_name1().toString());
            jsonObject.accumulate("facebook_access_token", person
                    .getFacebook_access_token1().toString());
            /*
             * JSONObject jsonObject = new JSONObject();
             * 
             * jsonObject.accumulate("hash_key", "Daily");
             * jsonObject.accumulate("fb_post_frequency",
             * "9afc15d212107f03d08037290631df5****");
             */
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the
            // content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                System.out.println(result);
            } else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }

    /**
     * Get Profile information by making request to Facebook Graph API
     * */
    @SuppressWarnings("deprecation")
    public void getProfileInformation() {
        mAsyncRunner.request("me", new RequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                Log.d("Profile", response);
                String json = response;
                try {
                    // Facebook Profile JSON data
                    JSONObject profile = new JSONObject(json);

                    // getting name of the user
                    person.setFacebook_user_name1(profile.getString("name"));

                    // getting id of the user
                    person.setFacebook_user_id1(profile.getString("id"));

                    // getting email of the user
                    person.setFacebook_emailid1(profile.getString("email"));

                    // final String user_id = profile.getString("")
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            fb_useremail = person.getFacebook_emailid1()
                                    .toString();
                            fb_userid = person.getFacebook_user_id1()
                                    .toString();
                            fb_username = person.getFacebook_user_name1()
                                    .toString();
                            //Toast.makeText(LoginActivity.this,"Name: " + fb_username + "\nEmail: "+ fb_useremail + "   id:"+ fb_userid, Toast.LENGTH_LONG).show();
                            new HttpAsyncTask()
                                    .execute("http://www.powe****************");
                                            + "/account/signup_map");
                            // new
                            // HttpAsyncTask().execute("http://www.powercheck.icloco.com/rest/index.php"+"/account/fbpostfrequecy");
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onIOException(IOException e, Object state) {
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                    Object state) {
            }

            @Override
            public void onMalformedURLException(MalformedURLException e,
                    Object state) {
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
            }
        });
    }

}
