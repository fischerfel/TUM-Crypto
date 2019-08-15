package com.xxxxxxx.rest;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.JerseyClientBuilder;

public class RestClient {

    private Client client;

    public RestClient(Properties properties) {
        super();
        ClientBuilder clientBuilder = new JerseyClientBuilder();
        SSLContext sslContext = null;
        TrustManager[] trustAllCerts = new X509TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
        }
        clientBuilder.sslContext(sslContext);
        client = clientBuilder.build();
    }

    public <T> T execRequest(String uri, Class<T> type) {
        return client.target(uri).request().get(type);
    }
}
