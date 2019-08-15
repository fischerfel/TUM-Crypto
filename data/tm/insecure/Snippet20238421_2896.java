package com.example.HttpsMy;

import android.os.Environment;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;


import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsClietn {
    HttpsClietn() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new NTCredentials("Grigoriy.Polyakov","password", "", "domain.kz"));

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);

        HttpGet httpget = new HttpGet("https://serveer.domain.kz");


       HttpClient client = HttpClientBuilder.create()
               .setSSLSocketFactory(getFactory())
               .setDefaultCredentialsProvider(credsProvider)
               .setSslcontext(getContext())
               .build();

        System.out.println(client.execute(httpget,context).getStatusLine());

    }

    SSLContext getContext() throws NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, CertificateException, UnrecoverableKeyException {
        //new File("key/keystore.p12"), "1234"
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream keyInput = new FileInputStream(new File(Environment.getExternalStorageDirectory().getPath()+"/kvv/keystore.p12"));
        keyStore.load(keyInput, "1234".toCharArray());
        keyInput.close();

        keyManagerFactory.init(keyStore, "1234".toCharArray());

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), trustAllCerts, new SecureRandom());

        return context;
    }

    SSLConnectionSocketFactory getFactory() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return new SSLConnectionSocketFactory(getContext());
    }
}
