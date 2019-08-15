package com.stackoverflow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;

public class Downloader {
    public static String getData(String address) throws Exception {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        // Create a host name verifier that always passes
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        // open connection
        URL page = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) page.openConnection();
        BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        // read text
        String line;
        StringBuffer text = new StringBuffer();
        while ( (line = buff.readLine()) != null ) {
            //System.out.println(line);
            text.append(line + "\n");
        }
        buff.close();

        return text.toString();
    }

    public static void main(String[] argv) throws Exception {
        String str = getData("https://expired.badssl.com/");
        System.out.println(str);
    }
}
