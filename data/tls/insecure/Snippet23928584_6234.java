import java.io.IOException;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.ClientParamsStack;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.entity.InputStreamEntity;
import java.security.SecureRandom;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;


HttpClient httpclient = new DefaultHttpClient();

// init SSL

// load your keystore
KeyStore keyStore = KeyStore.getInstance("jks"); 
keyStore.load(new FileInputStream("path_to_your_keystore"), "your_keystore_password".toCharArray());
// create keystore manager
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
keyManagerFactory.init(keyStore, password.toCharArray());

// load your truststore
KeyStore trustStore = KeyStore.getInstance("jks");
trustStore.load(new FileInputStream("path_to_your_truststore"), "your_trustore_password".toCharArray());
// create truststore manager
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
trustManagerFactory.init(trustStore);

// create the ssl context
SSLContext context = SSLContext.getInstance("SSL");
context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
SSLSocketFactory ssf = new SSLSocketFactory(ctx);

//register https protocol in httpclient's scheme registry
ClientConnectionManager ccm = httpclient.getConnectionManager();
SchemeRegistry sr = ccm.getSchemeRegistry();
sr.register(new Scheme("https", 443, ssf));

// create the request
HttpPost post = new HttpPost("https://your_service_endpoint_url")
def bais = new ByteArrayInputStream("<SOAP:Envelope>...your_xml</SOAP:Envelope>".getBytes("UTF-8"))
def reqEntity = new InputStreamEntity(bais, bais.available());
reqEntity.setContentType("application/xml");
post.setEntity(reqEntity);

// make the post and get the response
ResponseHandler responseHandler = new BasicResponseHandler();
def responseBody = httpclient.execute(post, responseHandler);
log.info responseBody;
