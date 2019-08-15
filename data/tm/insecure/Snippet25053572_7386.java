package test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestHttpUrlConnection {

    public static void main(final String... params) {
        final List<String> portsToCheck = Arrays.asList(new String[] {"443", "5989"});
        final List<String> openPorts = new ArrayList<String>();
        final String host = "<SOME_IP>";
        final int timeout = 5000;

        if (!portsToCheck.isEmpty()) {
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });

            for (final String port : portsToCheck) {
                HttpsURLConnection connection = null;
                OutputStreamWriter out = null;
                try {
                    connection = (HttpsURLConnection) new URL(
                        "https://" + host + ":" + Integer.valueOf(port)).openConnection();
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(timeout);
                    final OutputStream os = connection.getOutputStream();
                    out = new OutputStreamWriter(os, "UTF8");
                    out.close();
                    openPorts.add(port);
                } catch(final IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (final IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            System.out.print("Open ports: " + openPorts.toString());
        }
    }

    private static void trustAllHttpsCertificates() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[1]; 
            trustAllCerts[0] = new TrustAllManager();
            final SSLContext sc = SSLContext.getInstance("SSL"); 
            sc.init(null, trustAllCerts, null); 
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class TrustAllManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() { 
            return null; 
        }
        public void checkServerTrusted(final X509Certificate[] certs, final String authType) throws CertificateException {
            // Empty
        } 
        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            // Empty
        }
    }
}
