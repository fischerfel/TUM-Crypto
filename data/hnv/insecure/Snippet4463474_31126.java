    private static DefaultHttpClient createHttpClient(int port) {
    try {
        java.lang.System.setProperty(
                "sun.security.ssl.allowUnsafeRenegotiation", "true");

        // First create a trust manager that won't care.
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                // Don't do anything.
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                // Don't do anything.
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        };

        // Now put the trust manager into an SSLContext.
        // Supported: SSL, SSLv2, SSLv3, TLS, TLSv1, TLSv1.1
        SSLContext sslContext = SSLContext.getInstance("SSLv3");
        sslContext.init(null, new TrustManager[] { trustManager },
                new SecureRandom());

        // Use the above SSLContext to create your socket factory
        // (I found trying to extend the factory a bit difficult due to a
        // call to createSocket with no arguments, a method which doesn't
        // exist anywhere I can find, but hey-ho).
        SSLSocketFactory sf = new SSLSocketFactory(sslContext);
        // Accept any hostname, so the self-signed certificates don't fail
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        // Register our new socket factory with the typical SSL port and the
        // correct protocol name.
        //Scheme httpsScheme = new Scheme("https", sf, port);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", sf, port));

        HttpParams params = new BasicHttpParams();
        ClientConnectionManager cm = new SingleClientConnManager(params,
                schemeRegistry);

        return new DefaultHttpClient(cm, params);
    } catch (Exception ex) {
        Log.error("ERROR Creating SSL Connection: " + ex.getMessage());

        return null;
    }
}
