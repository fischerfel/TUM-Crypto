TrustStrategy acceptingTrustStrategy = new TrustStrategy() {

      @Override
      public boolean isTrusted(X509Certificate[] certificate, String authType) {
        return true;
      }
    };
    SSLContext sslContext = null;
    try {
      sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(null, acceptingTrustStrategy).build();
    } catch (Exception e) {
      // Handle error
    }
CloseableHttpAsyncClient httpClient =
        HttpAsyncClientBuilder.create().setConnectionManager(connManager)
            .setDefaultRequestConfig(HttpClientUtils.configForClient)
            .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            .setSSLContext(sslContext).setRedirectStrategy(LaxRedirectStrategy.INSTANCE).build();
