    package com.example.androidwsdltest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.ksoap2.HeaderProperty;
import org.ksoap2.transport.ServiceConnection;

import android.util.Log;

public class AndroidInsecureHttpsServiceConnectionSE implements
        ServiceConnection {
    private HttpsURLConnection connection;

    public AndroidInsecureHttpsServiceConnectionSE(String host, int port,
            String file, int timeout) throws IOException {
        // allowAllSSL();
        connection = (HttpsURLConnection) new URL("https", host, port, file)
                .openConnection();
        updateConnectionParameters(timeout);
    }

    private static TrustManager[] trustManagers;

    public static class EasyX509TrustManager implements X509TrustManager {

        private X509TrustManager standardTrustManager = null;

        /**
         * Constructor for EasyX509TrustManager.
         */
        public EasyX509TrustManager(KeyStore keystore)
                throws NoSuchAlgorithmException, KeyStoreException {
            super();
            TrustManagerFactory factory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(keystore);
            TrustManager[] trustmanagers = factory.getTrustManagers();
            if (trustmanagers.length == 0) {
                throw new NoSuchAlgorithmException("no trust manager found");
            }
            this.standardTrustManager = (X509TrustManager) trustmanagers[0];
        }

        /**
         * @see 
         *      javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate
         *      [],String authType)
         */
        public void checkClientTrusted(X509Certificate[] certificates,
                String authType) throws CertificateException {
            standardTrustManager.checkClientTrusted(certificates, authType);
        }

        /**
         * @see 
         *      javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate
         *      [],String authType)
         */
        public void checkServerTrusted(X509Certificate[] certificates,
                String authType) throws CertificateException {
            if ((certificates != null) && (certificates.length == 1)) {
                certificates[0].checkValidity();
            } else {
                standardTrustManager.checkServerTrusted(certificates, authType);
            }
        }

        /**
         * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
         */
        public X509Certificate[] getAcceptedIssuers() {
            return this.standardTrustManager.getAcceptedIssuers();
        }

    }

    public static class FakeX509TrustManager implements X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public boolean isClientTrusted(X509Certificate[] chain) {
            return true;
        }

        public boolean isServerTrusted(X509Certificate[] chain) {
            return true;
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }

    /**
     * Allow all SSL certificates by setting up a host name verifier that passes
     * everything and as well setting up a SocketFactory with the
     * #FakeX509TrustManager.
     */
    public static void allowAllSSL() {

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                return true;
            }

        });

        SSLContext context = null;

        if (trustManagers == null) {
            try {
                trustManagers = new TrustManager[] { new EasyX509TrustManager(
                        null) };
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (KeyStoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            Log.e("allowAllSSL", e.toString());
        } catch (KeyManagementException e) {
            Log.e("allowAllSSL", e.toString());
        }
        // HttpsURLConnection.setDefaultAllowUserInteraction(true);
        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }

    /**
     * update the connection with the timeout parameter as well as allowing SSL
     * if the Android version is 7 or lower (since these versions have a broken
     * certificate manager, which throws a SSL exception saying "Not trusted
     * security certificate"
     * 
     * @param timeout
     */
    private void updateConnectionParameters(int timeout) {
        connection.setConnectTimeout(timeout); // 20 seconds
        connection.setReadTimeout(timeout); // even if we connect fine we want
                                            // to time out if we cant read
                                            // anything..
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        allowAllSSL();

        /*
         * int buildVersion = Build.VERSION.SDK_INT; if (buildVersion <= 7) {
         * Log.d("Detected old operating system version " + buildVersion +
         * " with SSL certificate problems. Allowing " + "all certificates.",
         * String.valueOf(buildVersion)); allowAllSSL(); } else {
         * Log.d("Full SSL active on new operating system version ",
         * String.valueOf(buildVersion)); }
         */
    }

    public void connect() throws IOException {
        connection.connect();
    }

    public void disconnect() {
        connection.disconnect();
    }

    public List getResponseProperties() {
        Map properties = connection.getHeaderFields();
        Set keys = properties.keySet();
        List retList = new LinkedList();

        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = (String) i.next();
            List values = (List) properties.get(key);

            for (int j = 0; j < values.size(); j++) {
                retList.add(new HeaderProperty(key, (String) values.get(j)));
            }
        }

        return retList;
    }

    public void setRequestProperty(String key, String value) {
        // We want to ignore any setting of "Connection: close" because
        // it is buggy with Android SSL.
        if ("Connection".equalsIgnoreCase(key)
                && "close".equalsIgnoreCase(value)) {
            // do nothing
        } else {
            connection.setRequestProperty(key, value);
        }
    }

    public void setRequestMethod(String requestMethod) throws IOException {
        connection.setRequestMethod(requestMethod);
    }

    public OutputStream openOutputStream() throws IOException {
        return connection.getOutputStream();
    }

    public InputStream openInputStream() throws IOException {
        return connection.getInputStream();
    }

    public InputStream getErrorStream() {
        return connection.getErrorStream();
    }

    public String getHost() {
        return connection.getURL().getHost();
    }

    public int getPort() {
        return connection.getURL().getPort();
    }

    public String getPath() {
        return connection.getURL().getPath();
    }

}
