package util;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class CustomTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] cert, String authType) {
        return;
    }

    public void checkServerTrusted(X509Certificate[] cert, String authType) {
        return;
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
