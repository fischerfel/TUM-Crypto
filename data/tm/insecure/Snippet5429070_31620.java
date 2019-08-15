package com.halosys.TvAnyTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.crypto.NullCipher;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreen extends Activity {
        StringEntity entry;
        BasicHttpResponse  httpResponse;
         BufferedReader in = null;
        //protected KeyStore sslContext;
         KeyStore ksTrust;
         TrustManagerFactory tmf;
         SSLContext sslContext;
         URL url;
         HttpsURLConnection conn;

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


        Button closeButton = (Button)this.findViewById(R.id.button1);
        closeButton.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
            // Load the self-signed server certificate

              try {
                  HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                          public boolean verify(String hostname, SSLSession session) {
                                  return true;
                          }});
                  SSLContext context = SSLContext.getInstance("TLS");
                  context.init(null, new X509TrustManager[]{new X509TrustManager(){
                          public void checkClientTrusted(X509Certificate[] chain,
                                          String authType) throws CertificateException {}
                          public void checkServerTrusted(X509Certificate[] chain,
                                          String authType) throws CertificateException {}
                          public X509Certificate[] getAcceptedIssuers() {
                                  return new X509Certificate[0];
                          }}}, new SecureRandom());
                  HttpsURLConnection.setDefaultSSLSocketFactory(
                                  context.getSocketFactory());
          } catch (Exception e) { // should never happen
                  e.printStackTrace();
          }


          char[] passphrase = "ssltestcert".toCharArray();

        try {
            ksTrust = KeyStore.getInstance("BKS");
        } catch (KeyStoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
          try {
            ksTrust.load(getApplicationContext().getResources().openRawResource(R.raw.ssltestcert),
                           passphrase);
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (CertificateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
          try {
            tmf.init(ksTrust);
        } catch (KeyStoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

          // Create a SSLContext with the certificate

        /*try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
          try {
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
        } catch (KeyManagementException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }*/

          // Create a HTTPS connection

        try {
            url = new URL("https", "https://50.18.49.118/xmlrpcand", 443, "/ssltest");
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            conn = (HttpsURLConnection) url.openConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

          /* Uncomment the following line of code if you want to skip SSL */
          /* hostname verification.  But it should only be done for testing. */
          /* See http://randomizedsort.blogspot.com/2010/09/programmatically-disabling-java-ssl.html */
         // conn.setHostnameVerifier((HostnameVerifier) new NullCipher()); 
         //conn.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
         conn.setSSLSocketFactory(sslContext.getSocketFactory());

             //MyHttpClient obj= new MyHttpClient(HomeScreen.this);
              //obj.createClientConnectionManager();
            /* HttpClient httpclient = new DefaultHttpClient();  
           String  str2= "<?xml version=\"1.0\"?><methodCall><methodName>tva1.catalog.version</methodName><params></params></methodCall>";
            HttpPost httppost = new HttpPost("https://50.18.49.118/xmlrpcand");

            try {
                entry = new StringEntity(str2,HTTP.UTF_8);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            entry.setContentType("text/xml");
            httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httppost.setEntity(entry);
            try {
                httpResponse = (BasicHttpResponse) httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

             try {
                in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            try {
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String result = sb.toString(); //Responsed xml is stored in result
            System.out.print(result);

          }
*/        };
    });
        }      
}
