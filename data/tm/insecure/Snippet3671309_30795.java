package com.example;

import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URL;

public class HttpsNoVerify {
    public static void main(String... args) throws Exception {
        URL url = new URL(args[0]);

        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){}
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){}
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        IOUtils.copy(url.openStream(), System.out);
    }

    public String fetch(URL url) throws Exception {
        return new SubProcess(HttpsNoVerify.class).run(url.toString());
    }
}
