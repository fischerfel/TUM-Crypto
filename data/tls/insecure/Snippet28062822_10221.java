public static void main(final String[] args) throws Exception {

    TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmfactory.init((KeyStore) null);
    TrustManager[] tms = tmfactory.getTrustManagers();
    if (tms != null) {
        for (int i = 0; i < tms.length; i++) {
            final TrustManager tm = tms[i];
            if (tm instanceof X509TrustManager) {
                tms[i] = new TrustManagerDelegate((X509TrustManager) tm);
            }
        }
    }
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tms, null);

    CloseableHttpClient httpClient = HttpClients.custom()
            .setSslcontext(sslContext)
            .build();
    try {
        CloseableHttpResponse response = httpClient.execute(new HttpGet("https://google.com/"));
        try {
            // do something usefull
        } finally {
            response.close();
        }
    } catch (SSLException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof MyCertificateException) {
            X509Certificate[] chain = ((MyCertificateException) cause).getChain();
            for (X509Certificate cert: chain) {
                System.out.println(cert);
            }
        }
    }
}

static class TrustManagerDelegate implements X509TrustManager {

    private final X509TrustManager trustManager;

    TrustManagerDelegate(final X509TrustManager trustManager) {
        super();
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
            this.trustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException ex) {
            throw new MyCertificateException(chain, ex);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.trustManager.getAcceptedIssuers();
    }

}

static class MyCertificateException extends CertificateException {

    private final X509Certificate[] chain;

    MyCertificateException(final X509Certificate[] chain, final CertificateException ex) {
        super(ex);
        this.chain = chain;
    }

    public X509Certificate[] getChain() {
        return chain;
    }

}
