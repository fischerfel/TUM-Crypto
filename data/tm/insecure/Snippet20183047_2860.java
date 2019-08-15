import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class MyHttpClientConfigurer implements HttpClientConfigurer {
    protected final Logger l = LoggerFactory.getLogger(getClass());

    @Override
    public void configureHttpClient(HttpClient httpClient) {
l.debug("*******************");
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");

// set up a TrustManager that trusts everything
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    System.out.println("getAcceptedIssuers =============");
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                                               String authType) {
                    System.out.println("checkClientTrusted =============");
                }

                public void checkServerTrusted(X509Certificate[] certs,
                                               String authType) {
                    System.out.println("checkServerTrusted =============");
                }
            }}, new SecureRandom());

            SSLSocketFactory sf = new SSLSocketFactory(sslContext);
            Scheme httpsScheme = new Scheme("https", 443, sf);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(httpsScheme);

// apache HttpClient version >4.2 should use BasicClientConnectionManager
//            ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
////            HttpClient httpClient = new DefaultHttpClient(cm);
//            httpClient = new HttpClient();
//httpClient.setHttpConnectionManager(cm);
        } catch (Exception e) {
        }

    }
}
