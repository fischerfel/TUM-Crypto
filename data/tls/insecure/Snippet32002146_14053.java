package com.example.apiclient2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

private final Context mContext = this;
private TextView mTextView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTextView = (TextView) findViewById(R.id.textView);

    new APIRequest().execute();
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

private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {

    final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];

    return new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return originalTrustManager.getAcceptedIssuers();
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    try {
                        originalTrustManager.checkClientTrusted(certs, authType);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    try {
                        originalTrustManager.checkServerTrusted(certs, authType);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                }
            }
    };
}

private SSLSocketFactory getSSLSocketFactory(String keyStoreType, int keystoreResId)
        throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {

    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = getResources().openRawResource(keystoreResId);

    Certificate ca = cf.generateCertificate(caInput);
    caInput.close();

    if (keyStoreType == null || keyStoreType.length() == 0) {
        keyStoreType = KeyStore.getDefaultType();
    }
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, wrappedTrustManagers, null);

    return sslContext.getSocketFactory();
}

private class APIRequest extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {

        try {
            URL url = new URL("https://192.168.0.100/api/document");
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory("BKS", R.raw.mybks_cert);

            urlConnection.setSSLSocketFactory(sslSocketFactory);
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            return String.valueOf(urlConnection.getResponseCode());

        } catch (Exception e) {
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
        mTextView.setText(response);
    }
}
