import javax.net.ssl.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
public class SSLTest {
public static void main(String [] args) throws Exception {
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
    SSLContext.setDefault(ctx);
    URL url = new URL("https://www.1aauto.com/1961-64-chevy-bel-air-biscayne-impala-fuel-tank-with-strap-set/i/1afrk00087");
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    });
    System.out.println(conn.getResponseCode());
    conn.disconnect();
}
private static class DefaultTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
}
