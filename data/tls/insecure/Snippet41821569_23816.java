 import org.apache.http.client.HttpClient; 
 import org.apache.http.conn.ClientConnectionManager;  
 import org.apache.http.conn.scheme.Scheme; 
 import org.apache.http.conn.scheme.SchemeRegistry; 
 import org.apache.http.conn.ssl.SSLSocketFactory; 
 import org.apache.http.impl.client.DefaultHttpClient; 
 import java.io.IOException; import java.net.Socket; 
 import java.net.UnknownHostException; 
 import java.security.KeyManagementException; 
 import java.security.KeyStore;
 import java.security.KeyStoreException; 
 import java.security.NoSuchAlgorithmException; 
 import java.security.UnrecoverableKeyException; 
 import java.security.cert.CertificateException; 
 import java.security.cert.X509Certificate; import javax.net.ssl.SSLContext;
 import javax.net.ssl.TrustManager; 
 import javax.net.ssl.X509TrustManager; 
    public class ExSSLSocketFactory extends SSLSocketFactory { 
      SSLContext sslContext = SSLContext.getInstance("TLS"); 
      public ExSSLSocketFactory(KeyStore truststore) throws 
         NoSuchAlgorithmException, KeyManagementException,KeyStoreException,
         UnrecoverableKeyException {
         super(truststore);
     TrustManager x509TrustManager = new X509TrustManager() { 
     public void checkClientTrusted(X509Certificate[] chain, String authType)  
     throws CertificateException {
     } public void checkServerTrusted(X509Certificate[] chain, String authType)    
     throws CertificateException {
      } public X509Certificate[] getAcceptedIssuers()
     { 
return null; 
} 
}; 
sslContext.init(null, new TrustManager[] {
     x509TrustManager }, null);
 } public ExSSLSocketFactory(SSLContext
     context) throws KeyManagementException, NoSuchAlgorithmException,
     KeyStoreException, UnrecoverableKeyException {
 super(null); 
sslContext = context; 
} @Override public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
     UnknownHostException {
 return sslContext.getSocketFactory().createSocket(socket, host, port,
     autoClose); 
} @Override public Socket createSocket() throws
     IOException {
 return sslContext.getSocketFactory().createSocket(); 
}
     public static HttpClient getHttpsClient(HttpClient client) { 
try{
     X509TrustManager x509TrustManager = new X509TrustManager() 
{ 
@Override
     public void checkClientTrusted(X509Certificate[] chain, String
     authType) throws CertificateException { }
 @Override public void
     checkServerTrusted(X509Certificate[] chain, String authType) throws
     CertificateException {
 }
 @Override public X509Certificate[]
     getAcceptedIssuers() { 
return null; 
}
 }; 
SSLContext sslContext = SSLContext.getInstance("TLS"); sslContext.init(null, new
     TrustManager[]{x509TrustManager}, null); SSLSocketFactory
     sslSocketFactory = new ExSSLSocketFactory(sslContext);
     sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
     ClientConnectionManager clientConnectionManager =
     client.getConnectionManager(); SchemeRegistry schemeRegistry =
     clientConnectionManager.getSchemeRegistry();
     schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
     return new DefaultHttpClient(clientConnectionManager,
     client.getParams()); 
} catch (Exception ex) 
{ return null; 
} } }
