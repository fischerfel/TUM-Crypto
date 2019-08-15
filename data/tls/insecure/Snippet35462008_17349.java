final SSLContext sslContext;
try {
    sslContext = SSLContext.getInstance("TLS");
    final TrustManagerFactory javaDefaultTrustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    javaDefaultTrustManager.init((KeyStore)null);
    final TrustManagerFactory customCaTrustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    customCaTrustManager.init(getKeyStore());

    sslContext.init(
        null,
        new TrustManager[]{
            new TrustManagerDelegate(
                    (X509TrustManager)customCaTrustManager.getTrustManagers()[0],
                    (X509TrustManager)javaDefaultTrustManager.getTrustManagers()[0],
                    new TrustSelfSignedStrategy()
            )
        },
        secureRandom
    );

} catch (final NoSuchAlgorithmException ex) {
    throw new SSLInitializationException(ex.getMessage(), ex);
} catch (final KeyManagementException ex) {
    throw new SSLInitializationException(ex.getMessage(), ex);
}

SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);

PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
        RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build()
);
//maximum parallel requests is 500
cm.setMaxTotal(500);
cm.setDefaultMaxPerRoute(500);

CredentialsProvider cp = new BasicCredentialsProvider();
cp.setCredentials(
        new AuthScope(apiSettings.getIdcApiUrl(), 443),
        new UsernamePasswordCredentials(apiSettings.getAgencyId(), apiSettings.getAgencyPassword())
);

client = HttpClients.custom()
                    .setConnectionManager(cm)
                    .build();
