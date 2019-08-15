package com.test.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.os.AsyncTask;

public class TestConnection extends AsyncTask<String, String, String> {

static InputStream firstCertificate = null;
static InputStream secondCertificate = null;
static InputStream thirdCertificate = null;

private static String htmlString;

public void passCertificates(Context c){
    c.getAssets();

    try {
        firstCertificate = c.getAssets().open("certificate1.crt");
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        secondCertificate=c.getAssets().open("certificate2.crt");
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        thirdCertificate=c.getAssets().open("certificate3");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

@Override
protected String doInBackground(String... params) {
    String webHtmlData=null;

    String inpUrl=params[0];
    final String username=params[1];
    final String password=params[2];

    CertificateFactory cf = null;
    try {
        cf = CertificateFactory.getInstance("X.509");
    } catch (CertificateException e) {
        e.printStackTrace();
    }

    Certificate ca1 = null;
    Certificate ca2 = null;
    Certificate ca3 = null;
    try {
        ca1 = cf.generateCertificate(firstCertificate);
        ca2 = cf.generateCertificate(secondCertificate);
        ca3 = cf.generateCertificate(thirdCertificate);
    } catch (CertificateException e) {
        e.printStackTrace();
    }
    finally {
        try {
            firstCertificate.close();
            secondCertificate.close();
            thirdCertificate.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = null;
    try {
        keyStore = KeyStore.getInstance(keyStoreType);
    } catch (KeyStoreException e) {
        e.printStackTrace();
    }
    try {
        keyStore.load(null, null);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        keyStore.setCertificateEntry("ca1", ca1);
    } catch (KeyStoreException e) {
        e.printStackTrace();
    }
    try {
        keyStore.load(null, null);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        keyStore.setCertificateEntry("ca2", ca2);
    } catch (KeyStoreException e) {
        e.printStackTrace();
    }
    try {
        keyStore.load(null, null);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        keyStore.setCertificateEntry("ca3", ca3);
    } catch (KeyStoreException e) {
        e.printStackTrace();
    }

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = null;
    try {
        tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        tmf.init(keyStore);
    } catch (KeyStoreException e) {
        e.printStackTrace();
    }

    // Create an SSLContext that uses our TrustManager
    SSLContext context = null;
    try {
        context = SSLContext.getInstance("TLS");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        context.init(null, tmf.getTrustManagers(), null);
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

    //authentication credentials
    Authenticator myAuth = new Authenticator() 
    {
        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password.toCharArray());
        }
    };
    Authenticator.setDefault(myAuth);

    // Tell the URLConnection to use a SocketFactory from our SSLContext
    URL url = null;
    try {
        url = new URL(inpUrl);
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
    HttpsURLConnection urlConnection = null;
    try {
        urlConnection = (HttpsURLConnection)url.openConnection();
    } catch (IOException e) {
        e.printStackTrace();
    }
    urlConnection.setSSLSocketFactory(context.getSocketFactory());
    try {
        InputStream in = urlConnection.getInputStream();// my code fails here
        webHtmlData=readStream(in);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return webHtmlData;
}

public String readStream(InputStream in) {

    StringBuilder response = null;
    try {
        BufferedReader is = 
                new BufferedReader(new InputStreamReader(in));
        String inputLine;
        response = new StringBuilder();
        while ((inputLine = is.readLine()) != null) {
            response.append(inputLine);
        }
        is.close();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return response.toString();
}
