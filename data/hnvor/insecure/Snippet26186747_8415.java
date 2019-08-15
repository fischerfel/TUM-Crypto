package httpcomponents;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 *
 * @author mark.jones
 */
public class HttpTest3 {

    public static void main(String[] args) {
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            HttpHead httpHead = null;
            TrustStrategy trustStrategy = null;
            SSLContext sslContext = null;
            X509HostnameVerifier x509HostnameVerifier = null;
            LayeredConnectionSocketFactory sslConnectionSocketFactory = null;
            Registry<ConnectionSocketFactory> registry = null;
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;
            RequestConfig requestConfig = null;
            String audioURL = "https://ancientserver.com/webreports/audio.jsp?callID=338786512&authentication=98695279578B04166629C0AC0ABB49F0";

            httpHead = new HttpHead(audioURL);

            trustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] xcs, String authType) throws CertificateException {
                    return true;
                }
            };

            sslContext = SSLContexts
                    .custom()
                    .useSSL()
                    .loadTrustMaterial(null, trustStrategy)
                    .setSecureRandom(new SecureRandom())
                    .build();

            x509HostnameVerifier = new X509HostnameVerifier() {
                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                    //do nothing
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                    //do nothing                                                            //do nothing
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                    //do nothing
                }

                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            };

            //either one works
            //LayeredConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, x509HostnameVerifier);

            registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory)
                    .build();

            poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);

            requestConfig = RequestConfig
                    .custom()
                    .setConnectTimeout(5000)            //5 seconds
                    .setConnectionRequestTimeout(5000)
                    .setSocketTimeout(5000)
                    .build();

            closeableHttpClient = HttpClientBuilder
                    .create()
                    .setDefaultRequestConfig(requestConfig)
                    .setSslcontext(sslContext)
                    .setHostnameVerifier(x509HostnameVerifier)
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .setConnectionManager(poolingHttpClientConnectionManager)
                    .build();

            if (closeableHttpClient != null) {
                closeableHttpResponse = closeableHttpClient.execute(httpHead);
                if (closeableHttpResponse != null) {
                    int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
                    System.out.println(String.valueOf(statusCode));
                    System.out.println(closeableHttpResponse.getFirstHeader("Content-Type"));
                }
            }
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            System.out.println(noSuchAlgorithmException.getMessage());
        } catch (KeyStoreException keyStoreException) {
            System.out.println(keyStoreException.getMessage());
        } catch (KeyManagementException keyManagementException) {
            System.out.println(keyManagementException.getMessage());
        } catch (IOException iOException) {
            System.out.println(iOException.getMessage());
        } finally {
            if (closeableHttpResponse != null) {
                try {
                    closeableHttpResponse.close();
                } catch (IOException iOException) {
                    System.out.println(iOException.getMessage());
                }
            }
            if (closeableHttpClient != null) {
                try {
                    closeableHttpClient.close();
                } catch (IOException iOException) {
                    System.out.println(iOException.getMessage());
                }
            }
        }
    }
}
