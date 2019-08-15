TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmfactory.init((KeyStore) null);
final TrustManager[] tms = tmfactory.getTrustManagers();
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

CloseableHttpClient client = HttpClientBuilder.create()
        .setSSLContext(sslContext)
        .build();
