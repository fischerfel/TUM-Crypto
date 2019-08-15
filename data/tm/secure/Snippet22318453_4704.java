public static class SavingTrustManager implements X509TrustManager {

    private X509TrustManager parent;

    private X509Certificate[] chain;

    public SavingTrustManager(X509TrustManager parent) {
        this.parent = parent;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        parent.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        this.chain = chain;
        parent.checkServerTrusted(chain, authType);
    }

    public X509Certificate[] getChain() {
        return chain;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return parent.getAcceptedIssuers();
    }
}
