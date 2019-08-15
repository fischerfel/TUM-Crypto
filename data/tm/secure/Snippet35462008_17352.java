public class TrustManagerDelegate implements X509TrustManager {
    private final X509TrustManager mainTrustManager;
    private final X509TrustManager trustManager;

    public TrustManagerDelegate(X509TrustManager mainTrustManager, X509TrustManager trustManager) {
        this.mainTrustManager = mainTrustManager;
        this.trustManager = trustManager;
    }

    @Override
    public void checkClientTrusted(
            final X509Certificate[] chain, final String authType) throws CertificateException {
        this.trustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(
            final X509Certificate[] chain, final String authType) throws CertificateException {
        try {
            mainTrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException ex) {
            this.trustManager.checkServerTrusted(chain, authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.trustManager.getAcceptedIssuers();
    }

}
