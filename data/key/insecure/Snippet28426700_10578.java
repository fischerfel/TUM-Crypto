import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FitBitActivity extends Activity implements Serializable{
    WebView webView;
    static String authToken;
    Context context = this;
    ProgressDialog progressBar;
    String finalAuthToken = "";
    String finalAuthTokenSecrate = "";
    String finalEncodedUserID = "";
    String authVerifer = "";
    String tempAuthToken = "";
    String fitbitUser = "";
    SharedPreferences sharedPref;
    String userid;
    static String TAG = "FitBitActivity";

    String Authorization = ""; 
    String res= "";
    static String timestamp ;
    String auth_nonce="456236281" ; 
     static String nonce ;
   // String parameters;
    static String consumerKey= "XXXXXX your key XXXXX";
    private ProgressDialog pDialog;

     Random RAND ;
     private static final String HMAC_SHA1 = "HmacSHA1";


    @SuppressLint({ "NewApi", "NewApi", "NewApi" })
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitbit_activity);

        Long tsLong = System.currentTimeMillis()/1000;
        timestamp = tsLong.toString();

        RAND = new Random();
        nonce = timestamp + RAND.nextInt();


        generateSignature();


    }

    public static  void generateSignature() {
        // TODO Auto-generated method stub
        String base_string =
                "POST&https%3A%2F%2Fapi.fitbit.com%2Foauth%2Frequest_token&oauth_consumer_key%3D"
 +consumerKey+"%26"+"oauth_nonce%3D"+nonce+"%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D"+timestamp+"%26oauth_version%3D1.0";
        String key = "sign";


         try {
                Mac mac = Mac.getInstance("HmacSHA1");
                SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm());
                mac.init(secret);
                byte[] digest = mac.doFinal(base_string.getBytes());

                String enc = new String(digest);

                // Base 64 Encode the results
                String retVal = Base64.encodeBase64String(digest);

              // byte[] retVal = Base64.encode(base_string.getBytes(), Base64.NO_WRAP);

               // byte[] retVal = Base64.encodeBase64(base_string.getBytes()); 

                Log.e(TAG, "String: " + base_string);
                Log.e(TAG, "key: " + key);
                Log.e(TAG, "result: " + retVal.toString());             

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

    }


}
