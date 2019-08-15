import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    public static int ConnectionTimeoutInSeconds = 45;
    public static boolean DisableSSLcertificateCheck = true;

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    public static HttpClient getHttpClient() {
        try {

            HttpParams params = new BasicHttpParams();

            // Turn off stale checking.  Our connections break all the time anyway,
            // and it's not worth it to pay the penalty of checking every time.
            HttpConnectionParams.setStaleCheckingEnabled(params, false);

            // Default connection and socket timeout of 20 seconds.  Tweak to taste.
            HttpConnectionParams.setConnectionTimeout(params, ConnectionTimeoutInSeconds * 1000);
            HttpConnectionParams.setSoTimeout(params, ConnectionTimeoutInSeconds * 1000);
            HttpConnectionParams.setSocketBufferSize(params, 8192);

            // Don't handle redirects -- return them to the caller.  Our code
            // often wants to re-POST after a redirect, which we must do ourselves.
            HttpClientParams.setRedirecting(params, false);

            SSLSocketFactory mySSLSocketFactory = SSLSocketFactory.getSocketFactory();

            // disable ssl check on debug
            if (DisableSSLcertificateCheck ) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                mySSLSocketFactory = new MySSLSocketFactory(trustStore);
                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                mySSLSocketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            }

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", mySSLSocketFactory, 443));
            ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

            return new DefaultHttpClient(manager, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

}
