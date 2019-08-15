import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
public class DummyTrustManager implements X509TrustManager {
  public void checkClientTrusted(X509Certificate[] chain, String authType) {
    // Does not throw CertificateException: all chains trusted
    return;
  }
  public void checkServerTrusted(X509Certificate[] chain, String authType) {
    // Does not throw CertificateException: all chains trusted
    return;
  }
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
  }
}
