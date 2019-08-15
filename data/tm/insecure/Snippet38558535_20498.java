import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

class hi implements X509TrustManager {
hi() {
}

public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
}

public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
}

public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
}
}
