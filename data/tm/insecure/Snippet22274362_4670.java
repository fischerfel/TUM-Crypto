import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class RestClient {

private static final String BASE_URL = "https://YOUR_DOMAIN.COM/";

private static AsyncHttpClient client = new AsyncHttpClient();

public static void get(String url, RequestParams params,
        AsyncHttpResponseHandler responseHandler) {
    client.setSSLSocketFactory(getSSLSocketFactory());
    client.get(getAbsoluteUrl(url), params, responseHandler);
}

public static void post(String url, RequestParams params,
        AsyncHttpResponseHandler responseHandler) {
    client.setSSLSocketFactory(getSSLSocketFactory());
    client.post(getAbsoluteUrl(url), params, responseHandler);
}

public static void put(String url, RequestParams params,
        AsyncHttpResponseHandler responseHandler) {
    client.setSSLSocketFactory(getSSLSocketFactory());
    client.put(getAbsoluteUrl(url), params, responseHandler);
}

private static String getAbsoluteUrl(String relativeUrl) {
    return BASE_URL + relativeUrl;
}

public static class VoipemSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public VoipemSSLSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host,
                port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}

private static SSLSocketFactory getSSLSocketFactory() {
    KeyStore trustStore;
    SSLSocketFactory sslSocketFactory = null;
    try {
        trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        sslSocketFactory = new VoipemSSLSocketFactory(trustStore);
        sslSocketFactory
                .setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) { 
    }
    return sslSocketFactory;
}
}
