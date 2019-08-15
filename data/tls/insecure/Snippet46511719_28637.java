import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.log4j.Logger;

public class SSLContextHelper {

    private static final String KEY_STORE_TYPE="JKS";
    private static final String CLASS_NAME=SSLContextHelper.class.getName();
    private static final String TRANSPORT_SECURITY_PROTOCOL="TLS";
    private static final Logger logger=Logger.getLogger(SSLContextHelper.class);

    public static void enable(){
        String keystoreType = "JKS";
        InputStream keystoreLocation = null;
        char [] keystorePassword = null;
        char [] keyPassword = null;


        try {
              KeyStore keystore = KeyStore.getInstance(keystoreType);       
              keystore.load(keystoreLocation, keystorePassword);
              KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
              kmfactory.init(keystore, keyPassword);
              InputStream truststoreLocation = null;
              char [] truststorePassword = null;
              String truststoreType = KEY_STORE_TYPE;

              KeyStore truststore = KeyStore.getInstance(truststoreType);
              truststore.load(truststoreLocation, truststorePassword);
              TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

              KeyManager [] keymanagers = kmfactory.getKeyManagers();
            TrustManager [] trustmanagers =  tmfactory.getTrustManagers();

            SSLContext sslContext = SSLContext.getInstance(TRANSPORT_SECURITY_PROTOCOL);
            sslContext.init(keymanagers, trustmanagers, new SecureRandom());
            SSLContext.setDefault(sslContext);
        } catch (Exception e) {
            logger.error(CLASS_NAME+"Exception in SSL "+e.getMessage());
            e.printStackTrace();
        }
    }

    public static void disable() {
        try {
            SSLContext sslc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagerArray = { (TrustManager) new NullX509TrustManager() };
            sslc.init(null, trustManagerArray, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static class NullX509TrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            System.out.println();
        }
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            System.out.println();
        }

    }

    private static class NullHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
