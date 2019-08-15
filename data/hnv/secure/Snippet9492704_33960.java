import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

public class EasyHttpClient extends DefaultHttpClient {

  /**
   * Connection manager that uses our SSL certificate.
   */
  @Override
  protected ClientConnectionManager createClientConnectionManager() {
    SchemeRegistry registry = new SchemeRegistry();

    // Register for HTTP.
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

    // SSL Socket.
    SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
    sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

    // Register for HTTPS.
    registry.register(new Scheme("https", sslSocketFactory, 443));

    return new SingleClientConnManager(getParams(), registry);
  }
}
