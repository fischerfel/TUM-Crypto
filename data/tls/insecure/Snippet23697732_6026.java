package com.example.customssl;

import android.content.Context;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class CustomCAHttpsProvider {

    /**
     * Creates a {@link org.apache.http.client.HttpClient} which is configured to work with a custom authority
     * certificate.
     *
     * @param context       Application Context
     * @param certRawResId  R.raw.id of certificate file (*.crt). Should be stored in /res/raw.
     * @param allowAllHosts If true then client will not check server against host names of certificate.
     * @return Http Client.
     * @throws Exception If there is an error initializing the client.
     */
    public static HttpClient getHttpClient(Context context, int certRawResId, boolean allowAllHosts) throws Exception {


        // build key store with ca certificate
        KeyStore keyStore = buildKeyStore(context, certRawResId);

        // init ssl socket factory with key store
        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(keyStore);

        // skip hostname security check if specified
        if (allowAllHosts) {
            sslSocketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
        }

        // basic http params for client
        HttpParams params = new BasicHttpParams();

        // normal scheme registry with our ssl socket factory for "https"
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));

        // create connection manager
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

        // create http client
        return new DefaultHttpClient(cm, params);
    }

    /**
     * Creates a {@link javax.net.ssl.HttpsURLConnection} which is configured to work with a custom authority
     * certificate.
     *
     * @param urlString     remote url string.
     * @param context       Application Context
     * @param certRawResId  R.raw.id of certificate file (*.crt). Should be stored in /res/raw.
     * @param allowAllHosts If true then client will not check server against host names of certificate.
     * @return Http url connection.
     * @throws Exception If there is an error initializing the connection.
     */
    public static HttpsURLConnection getHttpsUrlConnection(String urlString, Context context, int certRawResId,
                                                           boolean allowAllHosts) throws Exception {

        // build key store with ca certificate
        KeyStore keyStore = buildKeyStore(context, certRawResId);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Create a connection from url
        URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

        // skip hostname security check if specified
        if (allowAllHosts) {
            urlConnection.setHostnameVerifier(new AllowAllHostnameVerifier());
        }

        return urlConnection;
    }

    private static KeyStore buildKeyStore(Context context, int certRawResId) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        // init a default key store
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        // read and add certificate authority
        Certificate cert = readCert(context, certRawResId);
        keyStore.setCertificateEntry("ca", cert);

        return keyStore;
    }

    private static Certificate readCert(Context context, int certResourceId) throws CertificateException, IOException {

        // read certificate resource
        InputStream caInput = context.getResources().openRawResource(certResourceId);

        Certificate ca;
        try {
            // generate a certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

        return ca;
    }

}
