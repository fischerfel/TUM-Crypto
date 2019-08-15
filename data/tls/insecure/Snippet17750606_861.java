 import javax.net.ssl.X509TrustManager
 import javax.net.ssl.SSLContext
 import java.security.cert.X509Certificate
 import javax.net.ssl.TrustManager
 import java.security.SecureRandom
 import org.apache.http.conn.ssl.SSLSocketFactory
 import org.apache.http.conn.scheme.Scheme
 import org.apache.http.conn.scheme.SchemeRegistry

 def http = new HTTPBuilder( "https://your_unsecure_certificate_host" )

 //=== SSL UNSECURE CERTIFICATE ===
 def sslContext = SSLContext.getInstance("SSL")              
 sslContext.init(null, [ new X509TrustManager() {public X509Certificate[]   
 getAcceptedIssuers() {null }
 public void checkClientTrusted(X509Certificate[] certs, String authType) { }
 public void checkServerTrusted(X509Certificate[] certs, String authType) { }
 } ] as TrustManager[], new SecureRandom())
 def sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
 def httpsScheme = new Scheme("https", sf, 443)
 http.client.connectionManager.schemeRegistry.register( httpsScheme )
 //================================
