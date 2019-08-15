import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class SSLTest {

public static void main(String [] args) throws Exception {
    System.setProperty("javax.net.debug" , "ssl");
    System.setProperty("javax.net.ssl.trustStorePassword","123456");
    System.setProperty("javax.net.ssl.trustStore","C:\\Projectos\\GapM\\WebServiceTes\\src\\truststore.jks");

    URL url = new URL("https://www.segurnet.pt");
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    });
    System.out.println(conn.getResponseCode());
    System.out.println(conn.getResponseMessage());

    conn.disconnect();
}

private static class DefaultTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}

}
