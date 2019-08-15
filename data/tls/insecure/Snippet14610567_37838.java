import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;

public class HttpClient {

private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);

private KeyStore keystore;
private String password;
private int MAX_TOTAL_CONNECTION;
private int MAX_PER_ROUTE;

public HttpClient(KeyStore keyStore, String keyPassword, int MAX_TOTAL_CONNECTION, int MAX_PER_ROUTE) {
    this.keystore = keyStore;
    this.password = keyPassword;
    this.MAX_TOTAL_CONNECTION = MAX_TOTAL_CONNECTION;
    this.MAX_PER_ROUTE = MAX_PER_ROUTE;
}

public org.apache.http.client.HttpClient get() {
    PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(getSchemeRegistry(this.keystore, this.password));
    connectionManager.setMaxTotal(MAX_TOTAL_CONNECTION);
    connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
    connectionManager.closeExpiredConnections();

    org.apache.http.client.HttpClient httpClient = new DefaultHttpClient(connectionManager);
    httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_READ_TIMEOUT_MILLISECONDS);
    return httpClient;
}

private static SchemeRegistry getSchemeRegistry(KeyStore keyStore, String keyPassword) {
    SchemeRegistry registry = new SchemeRegistry();
    try{
        TrustManager[] trustManagerArray = { new TautologicalX509TrustManager() };
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyPassword.toCharArray());

        SSLContext sslc = SSLContext.getInstance("TLS");
        sslc.init(kmf.getKeyManagers(), trustManagerArray, null);
                    SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslc, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, sslSocketFactory));
        return registry;
    }catch(Exception e){
        throw new RuntimeException(e.getMessage());
    }
}
}    
