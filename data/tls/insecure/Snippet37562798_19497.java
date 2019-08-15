import javax.net.ssl.*;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/***
 * Should only be used in development, this class will allow connections to an HTTPS server with unverified certificates. 
 * obviously this should not be used in the real world
 */
public class TrustModifier {
private static final TrustingHostnameVerifier TRUSTING_HOSTNAME_VERIFIER = new TrustingHostnameVerifier();
private static SSLSocketFactory factory;

/**
 * Call this with any HttpURLConnection, and it will modify the trust settings if it is an HTTPS connection.
 *
 * @param conn the {@link HttpURLConnection} instance
 * @throws KeyManagementException   if an error occurs while initializing the context object for the TLS protocol
 * @throws NoSuchAlgorithmException if no Provider supports a TrustManagerFactorySpi implementation for the TLS protocol.
 */
public static void relaxHostChecking(HttpURLConnection conn) throws KeyManagementException, NoSuchAlgorithmException {
    if (conn instanceof HttpsURLConnection) {
        HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
        SSLSocketFactory factory = prepFactory();
        httpsConnection.setSSLSocketFactory(factory);
        httpsConnection.setHostnameVerifier(TRUSTING_HOSTNAME_VERIFIER);
    }
}

 /**
 * Returns an {@link SSLSocketFactory} instance for the protocol being passed, this represents a secure communication context
 *
 * @return a {@link SSLSocketFactory} object for the TLS protocol
 * @throws NoSuchAlgorithmException if no Provider supports a TrustManagerFactorySpi implementation for the specified protocol.
 * @throws KeyManagementException   if an error occurs while initializing the context object
 */
static synchronized SSLSocketFactory prepFactory() throws NoSuchAlgorithmException, KeyManagementException {
    if (factory == null) {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[]{new AlwaysTrustManager()}, null);
        factory = ctx.getSocketFactory();
    }
    return factory;
}

private static final class TrustingHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

private static class AlwaysTrustManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
  }
}
