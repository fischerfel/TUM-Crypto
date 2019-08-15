    import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
    import java.io.IOException;
    import java.net.HttpURLConnection;
    import java.net.InetSocketAddress;
    import java.net.Proxy;
    import java.net.URL;
    import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 *
 * @author Aimable
 */
public class ConnectionFactory implements HttpURLConnectionFactory {

    Proxy proxy;

    String proxyHost;

    Integer proxyPort;

    SSLContext sslContext;

    public ConnectionFactory() {
    }

    public ConnectionFactory(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    private void initializeProxy() {
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    }

    @Override
    public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        initializeProxy();
        HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
        if (con instanceof HttpsURLConnection) {
            System.out.println("The valus is....");
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection(proxy);
            httpsCon.setHostnameVerifier(getHostnameVerifier());
            httpsCon.setSSLSocketFactory(getSslContext().getSocketFactory());
            return httpsCon;
        } else {
            return con;
        }

    }

    public SSLContext getSslContext() {
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new SecureTrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sslContext;
    }

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                return true;
            }
        };
    }

}
