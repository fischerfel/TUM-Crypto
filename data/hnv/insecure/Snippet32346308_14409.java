  public static HttpClient getNewHttpClient() {
    DefaultHttpClient httpclient = null;
    Log.i("check","getNewHttpClient   ");
    try 
    {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        int timeoutConnection = timeOut * 1000; // 10 sec
        HttpConnectionParams.setConnectionTimeout(params,timeoutConnection);
        HttpConnectionParams.setSoTimeout(params,timeoutConnection);
        HttpConnectionParams.setTcpNoDelay(params, true);


        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 9090));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        httpclient = new DefaultHttpClient(ccm, params);
    } catch (Exception e)
    {
        Log.i("check","Exception     "+e.getLocalizedMessage());
        httpclient = new DefaultHttpClient();
    }
    return httpclient;
}  

2.  

     import java.io.IOException;
     import java.net.Socket;
     import java.net.UnknownHostException;
     import java.security.KeyManagementException;
     import java.security.KeyStore;
     import java.security.KeyStoreException;
     import java.security.NoSuchAlgorithmException;
     import java.security.Principal;
     import java.security.UnrecoverableKeyException;
     import java.security.cert.CertificateException;
     import java.security.cert.X509Certificate;

     import javax.net.ssl.SSLContext;
     import javax.net.ssl.SSLException;
     import javax.net.ssl.SSLSession;
     import javax.net.ssl.SSLSocket;
     import javax.net.ssl.TrustManager;
     import javax.net.ssl.X509TrustManager;

     import org.apache.http.conn.ssl.SSLSocketFactory;
     import org.apache.http.conn.ssl.X509HostnameVerifier;

     import android.util.Log;


      public class MySSLSocketFactory extends SSLSocketFactory {
SSLContext sslContext = SSLContext.getInstance("TLS");

private String getValByAttributeTypeFromIssuerDN(String dn, String attributeType)
{
    String[] dnSplits = dn.split(","); 
    for (String dnSplit : dnSplits) 
    {
        if (dnSplit.contains(attributeType)) 
        {
            String[] cnSplits = dnSplit.trim().split("=");
            if(cnSplits[1]!= null)
            {
                return cnSplits[1].trim();
            }
        }
    }
    return "";
}

public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
    super(truststore);

    TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.e("check", "checkClientTrusted ");
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.e("check", "checkServerTrusted "+chain);
            Log.e("check", "authType "+authType);
            X509Certificate  c = chain[0];
            Principal p = c.getIssuerDN();
            String  pp = c.getIssuerDN().getName();
            String CN = getValByAttributeTypeFromIssuerDN(pp,"CN=");
            Log.i("check","CN  "+CN + "pp  "+pp + "P"+p);
            if( CN.equals("MC"))
            {


            }
            else
            {
                throw new CertificateException();
            }

            //c.checkValidity();
            //Principal p1 = c.getSubjectDN();
        }

        public X509Certificate[] getAcceptedIssuers() {
            Log.e("MySSLSocketFactory", "getAcceptedIssuers ");
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

     X509HostnameVerifier verifier = new X509HostnameVerifier() {

    public void verify(String string, SSLSocket ssls) throws IOException {
        Log.e("MySSLSocketFactory-", "X509HostnameVerifier-verify-1 ");
    }


    public void verify(String string, X509Certificate xc) throws SSLException {
        Log.e("MySSLSocketFactory-", "X509HostnameVerifier-verify-2");
    }


    public void verify(String string, String[] strings, String[] strings1) throws SSLException {
        Log.e("MySSLSocketFactory-", "X509HostnameVerifier-verify-3");
    }


    public boolean verify(String string, SSLSession ssls) {
        Log.e("MySSLSocketFactory-", "X509HostnameVerifier-verify-boolean ");
        return true;
    }
    };
   }
