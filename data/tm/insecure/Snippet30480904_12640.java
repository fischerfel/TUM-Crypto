static Scheme disableSSLCheck() {

        def sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, [new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            public void checkServerTrusted(X509Certificate[] certs, String authType) {}

            @Override
            X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0]
            }
        }] as TrustManager[], new SecureRandom())
        def sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
        def httpsScheme = new Scheme("https", sf, 443)
        httpsScheme

    }
