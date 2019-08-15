public class ConnectionFactory {

Proxy proxy;

String proxyHost;

Integer proxyPort;

public boolean canConnect = true;

private static final Logger log = Logger.getLogger("ReportPortal");

public ConnectionFactory() {
}

/**
 *
 * @return
 */
public static SSLContext getSslContext() {
    SSLContext sslContext = null;
    try {
        sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{new      SecureTrustManager()}, new SecureRandom());
    }
    catch (NoSuchAlgorithmException | KeyManagementException ex) {
        log.error("ERROR OCCURS", ex);
    }
    return sslContext;
}

/**
 *
 * @return
 */
public static HostnameVerifier getHostnameVerifier() {
    return (String hostname, javax.net.ssl.SSLSession sslSession) -> true;
}

public Boolean isHttps(String url) {

    if (url.startsWith("https://")) {
        return Boolean.TRUE;
    }
    else {
        return Boolean.FALSE;
    }
}
