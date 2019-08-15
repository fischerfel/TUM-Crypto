import java.security.KeyStore;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class HTTPUtils {
      /**
       * HttpClient
       * @param isHTTPS
       * @return
       */
      public static HttpClient getNewHttpClient(boolean isHTTPS) {
            try {
                  if(!isHTTPS){
                        return getNewHttpClient();
                  }
                  KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                  trustStore.load(null, null);
                  SSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
                  sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                  HttpParams params = new BasicHttpParams();
                  SchemeRegistry registry = new SchemeRegistry();
                  registry.register(new Scheme("https", sf, 443));

                  ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
                  return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                  return null;
            }
      }
      /**
       * HttpClient for http request
       * @return
       */
      private static HttpClient getNewHttpClient(){
            HttpParams params = new BasicHttpParams();
            return new DefaultHttpClient(params);
      }
}
