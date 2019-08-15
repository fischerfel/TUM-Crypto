import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class DummyTrustManager implements X509TrustManager {

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    /* (non-Javadoc)
    * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[], java.lang.String)
    */
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }
    /* (non-Javadoc)
    * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[], java.lang.String)
    */
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }
}
