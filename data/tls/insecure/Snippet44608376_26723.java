package com.example.so.tls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


/**
 * <p> https://stackoverflow.com/q/44607949/2862341 </p>
 * @author ravindra
 */
public class TestCustomTruststore {

    public static void main(String[] args) {

        String pathToTruststore = "/path/to/customTruststore.jks"; // truststore has the server certificate loaded
        try {
            File file = new File(pathToTruststore);
            KeyStore trustStore = KeyStore.getInstance("jks");
            trustStore.load(new FileInputStream(file), null);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            String urlStr = "https://example.com";
            URL url = new URL(urlStr);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setSSLSocketFactory(sslSocketFactory);

            httpsURLConnection.getInputStream();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


    }

}
