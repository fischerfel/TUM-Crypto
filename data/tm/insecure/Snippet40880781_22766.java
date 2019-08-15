  private static X509TrustManager createInsecureTrustManager() {
    return new X509TrustManager() {
      @Override public void checkClientTrusted(X509Certificate[] chain, String authType) {
      }

      @Override public void checkServerTrusted(X509Certificate[] chain, String authType) {
      }

      @Override public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    };
  }

  private static SSLSocketFactory createInsecureSslSocketFactory(TrustManager trustManager) {
    try {
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(null, new TrustManager[] {trustManager}, null);
      return context.getSocketFactory();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  private static HostnameVerifier createInsecureHostnameVerifier() {
    return new HostnameVerifier() {
      @Override public boolean verify(String s, SSLSession sslSession) {
        return true;
      }
    };
}
