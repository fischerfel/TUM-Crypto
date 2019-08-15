private static SSLContext createSSLContext(final KeyStore keyStore, final KeyStore trustStore) throws Exception {
    KeyManager[] keyManagers;
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, password("key"));
    keyManagers = keyManagerFactory.getKeyManagers();

    TrustManager[] trustManagers;
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);
    trustManagers = trustManagerFactory.getTrustManagers();

    SSLContext sslContext;
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, trustManagers, null);

    return sslContext;
}
