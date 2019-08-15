MongoClient mongoClient = new MongoClient(serverAddress, 
Collections.singletonList(mongoCredential), MongoClientOptions.builder().sslEnabled(true).socketFactory(getNoopSslSoketFactory()).build());


private static SSLSocketFactory getNoopSslSocketFactory() {
    SSLContext sslContext;
    try {
        sslContext = SSLContext.getInstance("SSL");

        // set up a TrustManager that trusts everything
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
        LOG.error("Couldn't create SSL Context for MongoDB connection", e);
        throw new RuntimeException(e);
    }
    return sslContext.getSocketFactory();
}
