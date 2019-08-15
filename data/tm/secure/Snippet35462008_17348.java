public class TrustManagerDelegate implements X509TrustManager {
    private final X509TrustManager mainTrustManager;
    private final X509TrustManager trustManager;
    private final TrustStrategy trustStrategy;

    public TrustManagerDelegate(X509TrustManager mainTrustManager, X509TrustManager trustManager, TrustStrategy trustStrategy) {
        this.mainTrustManager = mainTrustManager;
        this.trustManager = trustManager;
        this.trustStrategy = trustStrategy;
    }

    @Override
    public void checkClientTrusted(
            final X509Certificate[] chain, final String authType) throws CertificateException {
        this.trustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(
            final X509Certificate[] chain, final String authType) throws CertificateException {
        if (!this.trustStrategy.isTrusted(chain, authType)) {
            try {
                mainTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ex) {
                this.trustManager.checkServerTrusted(chain, authType);
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.trustManager.getAcceptedIssuers();
    }

}
