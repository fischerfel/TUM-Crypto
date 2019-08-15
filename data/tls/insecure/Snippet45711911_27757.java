package test_abc;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
public class TrustHttp {
public static void main(String[] args) throws Exception {
DefaultHttpClient httpClient = new DefaultHttpClient();
try {
SSLContext ctx = SSLContext.getInstance("TLS");
TrustManager[] trustManagers = getTrustManagers("jks", new FileInputStream(new File("C:\\Users\\akarki\\Desktop\\certi\\abcd\\mykeystore.jks")), "password1");
KeyManager[] keyManagers = getKeyManagers("pkcs12", new FileInputStream(new File("C:\\Users\\akarki\\Desktop\\certi\\abcd\\KeyStore.p12")), "password1");
ctx.init(keyManagers, trustManagers, new SecureRandom());
SSLSocketFactory factory = new SSLSocketFactory(ctx, new StrictHostnameVerifier());
ClientConnectionManager manager = httpClient.getConnectionManager();
manager.getSchemeRegistry().register(new Scheme("https", 443, factory));
//as before
HttpGet httpget = new HttpGet("https://URL:2376/images/json");
System.out.println("executing request" + httpget.getRequestLine());
HttpResponse response = httpClient.execute(httpget);
HttpEntity entity = response.getEntity();
System.out.println("----------------------------------------");
System.out.println(response.getStatusLine());
if (entity != null) {
System.out.println("Response content length: " + entity.getContentLength());
}
EntityUtils.consume(entity);
}finally {
// When HttpClient instance is no longer needed,
// shut down the connection manager to ensure
// immediate deallocation of all system resources
httpClient.getConnectionManager().shutdown();
}
}
protected static KeyManager[] getKeyManagers(String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws Exception {
KeyStore keyStore = KeyStore.getInstance(keyStoreType);
keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(keyStore, keyStorePassword.toCharArray());
return kmf.getKeyManagers();
}
protected static TrustManager[] getTrustManagers(String trustStoreType, InputStream trustStoreFile, String trustStorePassword) throws Exception {
KeyStore trustStore = KeyStore.getInstance(trustStoreType);
trustStore.load(trustStoreFile, trustStorePassword.toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(trustStore);
return tmf.getTrustManagers();
}
}
