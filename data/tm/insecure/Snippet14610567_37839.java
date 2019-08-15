import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TautologicalX509TrustManager  implements X509TrustManager {

private static final X509Certificate[] EMPTY_CERTIFICATES = new X509Certificate [0];

public void checkClientTrusted(X509Certificate[] arg0, String arg1)
    throws CertificateException {
}

public void checkServerTrusted(X509Certificate[] arg0, String arg1)
        throws CertificateException {
}

public X509Certificate[] getAcceptedIssuers() {
    return EMPTY_CERTIFICATES;
}



}    
