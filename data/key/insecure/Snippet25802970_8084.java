package com.example.api_tester;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    APICall api;
    ApiSecurity hash_security;

    TextView url;
    TextView api_result;
    TextView url_call;
    private String hardCodedUrl = " MY API URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Action Bar min target apit set to 11
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");

        ...

        test();

    }//end - onCreate




        private void test(){           
            try {
                String secret = "acbdef";
                String message = "api_key=abcd123&access_token=123abc";

                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
                sha256_HMAC.init(secret_key);            

                String hash = Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes()));
                Log.e(TAG, "Result=> " + hash);
            }
            catch (Exception e) {
                Log.e(TAG, "Error=> " + e);
            }
        }//end test

    ...
