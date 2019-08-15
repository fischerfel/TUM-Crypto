import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class DummyX509TrustManager implements X509TrustManager {

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
            throws CertificateException {
    }

    @Override
    public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
            throws CertificateException {
    }
};


final TrustManager[] trustAllCerts = new TrustManager[] { new DummyX509TrustManager() };
try {
    SSLContext sslContext= SSLContext.getInstance("SSL"); 
    sslContext.init(null, trustAllCerts, null);

    CloseableHttpClient client = HttpClients.custom()
        .setRedirectStrategy(new LaxRedirectStrategy()) 
        .setSslcontext(sslContext)   
        .setConnectionManager(connMgr)
        .build();
} catch (KeyManagementException e) {
    throw new IOException(e.getMessage());
} catch (NoSuchAlgorithmException e) {
    throw new IOException(e.getMessage());
}
