package com.g4apps.secure.android.sslclient;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.content.Context;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class SSLclient {


    public final static String authenticate(Context context) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String output=null;

        Security.addProvider(new BouncyCastleProvider());
        try {
            KeyStore trustStore  = KeyStore.getInstance("BKS");
            InputStream instream = context.getResources().getAssets().open("my.truststore");
            try {
                trustStore.load(instream, "dysan100".toCharArray());
            } finally {
                try { instream.close(); } catch (Exception ignore) {}
            }
            KeyStore keystore = KeyStore.getInstance("BKS");
            InputStream keystream = context.getResources().getAssets().open("my.keystore.bks");
            try {
                keystore.load(keystream, "dysan100".toCharArray());
            } finally {
                try { keystream.close(); } catch (Exception ignore) {}
            }

            SSLSocketFactory socketFactory = new SSLSocketFactory(keystore,"dysan100",trustStore);
            socketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
            Scheme sch = new Scheme("https", socketFactory, 443);
            httpclient.getConnectionManager().getSchemeRegistry().register(sch);

            HttpGet httpget = new HttpGet("https://192.168.1.123/test.php");

            System.out.println("executing request" + httpget.getRequestLine());

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
                output=EntityUtils.toString(entity);
                System.out.println(output);
                return output;

            }


        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }

        return null;
    }

}
