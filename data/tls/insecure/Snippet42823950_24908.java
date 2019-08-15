public static ClientConfig configureClient() {
    TrustManager[ ] certs = new TrustManager[ ] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            }
    };
    SSLContext ctx = null;
    try {
        ctx = SSLContext.getInstance("SSL");
        ctx.init(null, certs, new SecureRandom());
    } catch (java.security.GeneralSecurityException ex) {
    }
    HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

    ClientConfig config = new DefaultClientConfig();
    try {
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
                new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                },
                ctx
        ));
    } catch(Exception e) {
    }

    return config;
}
