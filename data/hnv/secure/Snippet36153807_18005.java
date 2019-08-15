import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.example.firstapp.R;

public class SimpleHttpClient {
 /** The time it takes for our client to timeout */
    public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds

    /** Single instance of our HttpClient */
    private static HttpClient mHttpClient;

    /**
     * Get our single instance of our HttpClient object.
     *
     * @return an HttpClient object with connection parameters set
     */
    private static HttpClient getHttpClient() {
        if (mHttpClient == null) {
         //sets up parameters
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "utf-8");
            params.setBooleanParameter("http.protocol.expect-continue", false);
            //registers schemes for both http and https
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", newSslSocketFactory(), 443));
            ClientConnectionManager manager = new ThreadSafeClientConnManager(params, registry);
            mHttpClient = new DefaultHttpClient(manager, params);
        }
        return mHttpClient;
    }

    private static SSLSocketFactory newSslSocketFactory() {
        try {
          KeyStore trusted = KeyStore.getInstance("BKS");
          InputStream in = LoginLayout.getAppContext().getResources().openRawResource(R.raw.keystore);
          try {
         // Keystore password comes in place of 222222
            trusted.load(in, "222222".toCharArray());
          } finally {
            in.close();
          }
          /*
           * If you use STRICT_HOSTNAME_VERIFIER, the the host name in the URL should match with
           * the host name in the server certificate. In this application it is 192.168.1.3
           * 
           * If you do not want to check the host name and simply want to connect to the URL, then use ALLOW_ALL_HOSTNAME_VERIFIER
           * 
           */
          //HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
          HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
          SSLSocketFactory socketFactory = new SSLSocketFactory(trusted);
          socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
          return socketFactory;
        } catch (Exception e) {
          throw new AssertionError(e);
        }
      }

    /**
     * Performs an HTTP Post request to the specified url with the
     * specified parameters.
     *
     * @param url The web address to post the request to
     * @param postParameters The parameters to send via the request
     * @return The result of the request
     * @throws Exception
     */
    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();
            return result;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Performs an HTTP GET request to the specified url.
     *
     * @param url The web address to post the request to
     * @return The result of the request
     * @throws Exception
     */
    public static String executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String result = sb.toString();
            return result;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
