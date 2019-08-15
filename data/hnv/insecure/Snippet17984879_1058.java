package com.example.testandroidapp;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    Button button1;

    private class ConnectionTask extends AsyncTask<String, Integer, String> {
        protected void onPostExecute(String result) {
            Log.d("MY_LOG", "Finished loading end point");
        }

        protected String doInBackground(String... arg) {
            String url = arg[0];

            Log.d("MY_LOG", "Started loading end point");

            DefaultHttpClient hc = null;
            String data = null;

            try {
                InputStream inputStream = MainActivity.class
                        .getResourceAsStream("service.cashyr.com.crt");

                if (inputStream != null) {
                    Log.d("MY_LOG", "Loaded certificate file successfully");

                    Certificate myCert = CertificateFactory.getInstance(
                            "X.509").generateCertificate(inputStream);

//                  Log.d("MY_LOG", "Certificate contents:" + myCert.toString());
                    Log.d("MY_LOG", "Certificate object loaded");

                    KeyStore keyStore = KeyStore.getInstance(KeyStore
                            .getDefaultType());
                    Log.d("MY_LOG", "Obtained keystore");
                    keyStore.load(null, null);
                    Log.d("MY_LOG", "keystore loaded");
                    keyStore.setCertificateEntry("myCert", myCert);
                    Log.d("MY_LOG", "set certificate entry");

                    SSLSocketFactory sf = new EasySSLSocketFactory(keyStore);
                    Log.d("MY_LOG", "created SSL socket factory");
                    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    Log.d("MY_LOG", "set host name verifier");

                    HttpParams params = new BasicHttpParams();
                    HttpProtocolParams.setVersion(params,
                            HttpVersion.HTTP_1_1);
                    HttpProtocolParams
                            .setContentCharset(params, HTTP.UTF_8);
                    Log.d("MY_LOG", "set up HTTP params");

                    SchemeRegistry registry = new SchemeRegistry();
                    registry.register(new Scheme("http", PlainSocketFactory
                            .getSocketFactory(), 80));
                    registry.register(new Scheme("https", sf, 8443));
                    Log.d("MY_LOG", "set up sche registry");

                    ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                            params, registry);
                    Log.d("MY_LOG", "created client connection manager object");

                    hc = new DefaultHttpClient(ccm, params);
                    Log.d("MY_LOG", "create default http client object for sending http request");

                    // Prepare a request object
                    HttpGet httpget = new HttpGet(url);

                    // Execute the request
                    HttpResponse response;

                    InputStream instream = null;

                    // // setSeeMoreDealsButton(context, false);
                    response = hc.execute(httpget);
                    Log.d("MY_LOG", "Sent https (SSL) get request.");

                    HttpEntity entity = response.getEntity();

                    instream = entity.getContent();
                    if (instream != null) {
                        data = Utils.convertStreamToString(instream);
                        Log.d("MY_LOG", "HTTPS response:  \n" + data);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

                hc = new DefaultHttpClient();
            } catch (Exception e) {
                e.printStackTrace();

                hc = new DefaultHttpClient();
            }

            return data;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String url = "https://service.cashyr.com:8443/PolarBear-0.0.1-SNAPSHOT/get_deals_dist/33.7445273910949/-118.10924671590328/12/0/aaaa";
                new ConnectionTask().execute(url);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
