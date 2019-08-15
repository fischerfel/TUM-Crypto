public static Client createIgnoreSSLClient() {
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.connectorProvider(new HttpUrlConnectorProvider());
    SSLContext sslcontext;
    try {
        sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }

        }}, new java.security.SecureRandom());
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    return ClientBuilder.newBuilder()
            .sslContext(sslcontext)
            .hostnameVerifier(createDummyHostnameVerifier())
            .withConfig(clientConfig)
            .build();
}   
