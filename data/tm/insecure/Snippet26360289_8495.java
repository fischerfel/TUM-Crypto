public class X509TrustManager implements X509TrustManager {

private final static String TAG = "X509TrustManager";

private static final boolean DEAFULT_TRUST_ALL_SSL_CONNECTIONS = true;

private X509TrustManager standardTrustManager = null;

private boolean trustAllSSLConnections;

/**
 * Constructor for EasyX509TrustManager.
 */
public X509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {

    trustAllSSLConnections = DEAFULT_TRUST_ALL_SSL_CONNECTIONS;

    TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    factory.init(keystore);
    TrustManager[] trustmanagers = factory.getTrustManagers();
    if (trustmanagers.length == 0) {
        throw new NoSuchAlgorithmException("no trust manager found");
    }
    this.standardTrustManager = (X509TrustManager) trustmanagers[0];
}

@Override
public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
    standardTrustManager.checkClientTrusted(certificates, authType);
}

/**
 * verified the server certificate
 */
@Override
public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {


        X509Certificate certificate = certificates[0];
        byte[] bytes = certificate.getTBSCertificate();

        // Compare your the certificate’s bytes to yours hardcoded certificate.         
}

/**
 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
 */
@Override
public X509Certificate[] getAcceptedIssuers() {
    return this.standardTrustManager.getAcceptedIssuers();
}
