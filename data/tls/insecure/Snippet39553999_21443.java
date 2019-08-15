package com.domain.myapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class LoginScreen extends AppCompatActivity {

Context ctx          = null;
InputStream inStream = null;
HurlStack hurlStack  = null;

EditText username    = null;
EditText password    = null;
String loginStatus   = null;

public LoginScreen() {

    try {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance("BKS");
        inStream = ctx.getApplicationContext().getResources().openRawResource(R.raw.mytruststore);
        ks.load(inStream, null);
        inStream.close();
        tmf.init(ks);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        hurlStack = new HurlStack(null, sslSocketFactory);
    } catch (Exception e){
        Log.d("Exception:",e.toString());
    }
}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login_screen);
    username = (EditText) findViewById(R.id.user);
    password = (EditText) findViewById(R.id.passwd);
}

public void login(View view) {

    RequestQueue queue = Volley.newRequestQueue(this, hurlStack);
    final String url = "https://myserver.domain.com/app/login";

    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    loginStatus = "OK";
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", String.valueOf(error));
                    loginStatus = "NOK";
                }
            }
    ) {
        @Override
        protected Map<String, String> getParams()
        {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("username", String.valueOf(user));
            params.put("domain", String.valueOf(passwd));

            return params;
        }
    };
    queue.add(postRequest);

    if (loginStatus == "OK") {
        Intent intent = new Intent(LoginScreen.this, OptionScreen.class);
        startActivity(intent);
    } else {
        Toast.makeText(getApplicationContext(), "Login failed",Toast.LENGTH_SHORT).show();
    }
}

}
