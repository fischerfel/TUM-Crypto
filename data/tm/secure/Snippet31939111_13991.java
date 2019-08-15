public class CustomTrustManager2 implements X509TrustManager {

private final X509TrustManager originalX509TrustManager;
private final KeyStore trustStore;


public CustomTrustManager2(KeyStore trustStore) throws NoSuchAlgorithmException,
        KeyStoreException {
    this.trustStore = trustStore;

    TrustManagerFactory originalTrustManagerFactory = TrustManagerFactory.getInstance("X509");
    originalTrustManagerFactory.init(this.trustStore);

    TrustManager[] originalTrustManagers = originalTrustManagerFactory.getTrustManagers();
    originalX509TrustManager = (X509TrustManager) originalTrustManagers[0];
}

public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
    try {
        originalX509TrustManager.checkServerTrusted(chain, authType);
    } catch (CertificateException originalException) {
        originalException.printStackTrace();
        try {
            X509Certificate[] reorderedChain = reorderCertificateChain(chain);
            CertPathValidator validator = CertPathValidator.getInstance("PKIX");
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            CertPath certPath = factory.generateCertPath(Arrays.asList(reorderedChain));
            PKIXParameters params = new PKIXParameters(trustStore);
            params.setRevocationEnabled(false);
            validator.validate(certPath, params);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw originalException;
        }
    }

}
