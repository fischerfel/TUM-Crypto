import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.util.Log;

/**
 * This class will perform all network related calls like post, get and put.
 */
public class NetworkUtility {
    protected static final String TAG = NetworkUtility.class.getSimpleName();
    /**
     * Connection timeout. 15 seconds
     */
    private static final int HTTP_CONNECTION_TIMEOUT = 150000;

    /**
     * Returns Default HTTP client with socket factories initialised.
     * 
     * @param context
     * @param targetUrl
     *            to do request
     * @return Default HTTP Client
     */
    private static HttpClient getDefaultHttpClient(Context context,
            String targetUrl) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,
                HTTP_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_CONNECTION_TIMEOUT);
        try {
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", new MySSLSocketFactory(
                    context.getApplicationContext(), targetUrl), 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient(params);
        }
    }

    /**
     * TrustManager to accept all certificates. It does not do any certificates
     * validation.
     * 
     * TODO: Once we have actual certificates this implementation should be
     * changed accordingly.
     */
    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager mOriginalX509TrustManager;
        private Context mContext;
        private String mTargetUrl;

        /**
         * @param context
         *            - application context.
         * @param targetUrl
         *            - to do request.
         */
        public MyTrustManager(Context context, String targetUrl) {
            try {
                this.mContext = context;
                this.mTargetUrl = targetUrl;
                TrustManagerFactory originalTrustManagerFactory = TrustManagerFactory
                        .getInstance("X509");
                originalTrustManagerFactory.init((KeyStore) null);
                TrustManager[] originalTrustManagers = originalTrustManagerFactory
                        .getTrustManagers();
                this.mOriginalX509TrustManager = (X509TrustManager) originalTrustManagers[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void checkClientTrusted(X509Certificate[] cert, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] cert, String authType)
                throws CertificateException {
            try {
                // Verify if the certificate has been trusted.
                // This validation will pass if certificate has
                // been added by user or system.
                mOriginalX509TrustManager.checkServerTrusted(cert, authType);
            } catch (CertificateException originalException) {
                // Certificate has not present into trust zone.
                // Find first certificate from the array of certificates which
                // needs to install.
                X509Certificate certificate = getCertificateToInstall(cert);
                Log.d(TAG, "Showing dialog for certificate exception...");
                // Show dialog where user can install this certificate
                SSLCertificateErrorDialogActivity.show(this.mContext,
                        certificate, this.mTargetUrl);
                throw originalException;
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * Get certificate to be installed from the given list of certificates. It
     * iterates all certificates from CA and if a certificate, from the given
     * array is not present into CA, this method returns that certificate.
     * 
     * @param certificates
     * @return {@link X509Certificate} to install.
     */
    private static X509Certificate getCertificateToInstall(
            X509Certificate[] certificates) {
        X509Certificate result = null;
        try {
            KeyStore ks = KeyStore.getInstance("AndroidCAStore");
            if (ks != null) {
                ks.load(null, null);
                boolean certFound = false;
                for (X509Certificate certificate : certificates) {
                    Enumeration<String> aliases = ks.aliases();
                    while (aliases.hasMoreElements()) {
                        String alias = (String) aliases.nextElement();
                        X509Certificate cert = (X509Certificate) ks
                                .getCertificate(alias);
                        if (certificate.equals(cert) == true) {
                            certFound = true;
                            break;
                        }
                    }
                    if (certFound == false) {
                        Log.d(TAG, "Not found certificate");
                        // Need to install this certificate
                        result = certificate;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private static class MySSLSocketFactory extends SSLSocketFactory {
        private javax.net.ssl.SSLSocketFactory mFactory;

        public MySSLSocketFactory(Context context, String targetUrl)
                throws KeyManagementException, NoSuchAlgorithmException,
                KeyStoreException, UnrecoverableKeyException {
            super((KeyStore) null);
            try {
                SSLContext sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[] { new MyTrustManager(
                        context, targetUrl) }, null);
                mFactory = sslcontext.getSocketFactory();
                setHostnameVerifier(new AllowAllHostnameVerifier());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public Socket createSocket() throws IOException {
            return mFactory.createSocket();
        }

        @Override
        public Socket createSocket(Socket socket, String s, int i, boolean flag)
                throws IOException {
            return mFactory.createSocket(socket, s, i, flag);
        }
    }
}
