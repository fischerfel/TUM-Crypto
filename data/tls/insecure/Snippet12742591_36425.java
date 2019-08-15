import java.io.IOException;

    import java.net.Socket;
    import java.security.KeyManagementException;
    import java.security.KeyStoreException;
    import java.security.NoSuchAlgorithmException;
    import java.security.UnrecoverableKeyException;
    import java.security.cert.CertificateException;
    import java.security.cert.X509Certificate;

    import javax.net.ssl.SSLContext;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509TrustManager;

    import org.apache.http.client.ClientProtocolException;
    import org.apache.http.client.HttpClient;
    import org.apache.http.client.methods.HttpPost;
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

    public class HttpsClientBuilder {
        public static DefaultHttpClient getBelieverHttpsClient() {

            DefaultHttpClient client = null;

            SchemeRegistry Current_Scheme = new SchemeRegistry();
            Current_Scheme.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            try {
                Current_Scheme.register(new Scheme("https", new Naive_SSLSocketFactory(), 8443));
            } catch (KeyManagementException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (KeyStoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            HttpParams Current_Params = new BasicHttpParams();
            int timeoutConnection = 8000;
            HttpConnectionParams.setConnectionTimeout(Current_Params, timeoutConnection);
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(Current_Params, timeoutSocket);
            ThreadSafeClientConnManager Current_Manager = new ThreadSafeClientConnManager(Current_Params, Current_Scheme);
            client = new DefaultHttpClient(Current_Manager, Current_Params);
            //HttpPost httpPost = new HttpPost(url);
            //client.execute(httpPost);

         return client;
         }

    public static class Naive_SSLSocketFactory extends SSLSocketFactory
    {
        protected SSLContext Cur_SSL_Context = SSLContext.getInstance("TLS");

        public Naive_SSLSocketFactory ()
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException
        {
            super(null, null, null, null, null, (X509HostnameVerifier)null);
            Cur_SSL_Context.init(null, new TrustManager[] { new X509_Trust_Manager() }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                boolean autoClose) throws IOException
        {
            return Cur_SSL_Context.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException
        {
            return Cur_SSL_Context.getSocketFactory().createSocket();
        }
    }

    private static class X509_Trust_Manager implements X509TrustManager
    {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    };
}
