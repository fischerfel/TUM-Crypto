 import java.io.DataInputStream;
 import java.io.DataOutputStream;
 import java.io.IOException;
 import java.net.URL;
 import java.net.URLEncoder;
 import java.security.cert.CertificateException;
 import java.security.cert.X509Certificate;
 import javax.net.ssl.HttpsURLConnection;
 import javax.net.ssl.X509TrustManager;
 public class Https {
public static void main(String[] args) throws IOException {
    sendPost("https://*", "MBSUV aa123");
}
public  static void sendPost(final String request, final String urlParameters) throws IOException {
    URL url = new URL(request); 
    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();  
    connection.setHostnameVerifier( new AlwaysTrustHostnameVerifier() );
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setInstanceFollowRedirects(false); 
    connection.setRequestMethod("POST"); 
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
    connection.setRequestProperty("charset", "utf-8");
    connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
    connection.setUseCaches (false);
    DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
    wr.writeBytes(urlParameters);
    wr.flush();
    wr.close();
    connection.disconnect();        
}
}
class AlwaysTrustHostnameVerifier implements X509TrustManager 
{
    public void checkClientTrusted( X509Certificate[] x509 , String authType ) throws CertificateException { /* nothing */ }
    public void checkServerTrusted( X509Certificate[] x509 , String authType ) throws CertificateException { /* nothing */ }
    public X509Certificate[] getAcceptedIssuers() { return null; }      
}
