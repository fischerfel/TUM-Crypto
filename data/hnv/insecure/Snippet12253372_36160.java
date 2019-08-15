package com.drawcoders.saldomovistarchile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SaldoMovistarChileActivity extends Activity {
    TextView statusText;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        statusText = (TextView) findViewById(R.id.statusText);
        login();
    }

    void login(){       
        try {
            HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier)hostnameVerifier);
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mngr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mngr, client.getParams());

            HttpPost post = new HttpPost("https://autoservicio.movistar.cl/login/loginTop");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("rut", "1000000"));
            nameValuePairs.add(new BasicNameValuePair("dv", "0"));
            nameValuePairs.add(new BasicNameValuePair("idRut", "10000000-0"));
            nameValuePairs.add(new BasicNameValuePair("clave", "00000000"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(post);

            // HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String responseText = EntityUtils.toString(entity);
            statusText.setText("Finalizado!");

        } catch (UnsupportedEncodingException e) {
            statusText.setText("Error: " + e.getMessage().toString());
        } catch (ClientProtocolException e) {
            statusText.setText("Error: " + e.getMessage().toString());
        } catch (IOException e) {
            statusText.setText("Error: " + e.getMessage().toString());
        }
    }
}
