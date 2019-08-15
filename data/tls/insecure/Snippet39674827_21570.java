import com.sun.net.ssl.HostnameVerifier;
import com.sun.net.ssl.HttpsURLConnection;
import com.sun.net.ssl.SSLContext;
import com.sun.net.ssl.TrustManager;
import com.sun.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jstein
 */
public class SSLHelper {

    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts;
            trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public boolean isClientTrusted(java.security.cert.X509Certificate[] xcs) {
                    return true;
                }

                @Override
                public boolean isServerTrusted(java.security.cert.X509Certificate[] xcs) {
                    return true;
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid;
            allHostsValid = new HostnameVerifier() {

                @Override
                public boolean verify(String string, String string1) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
