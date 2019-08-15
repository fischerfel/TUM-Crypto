SSLContext sslContext = null;
try {
  sslContext = SSLContext.getInstance("SSL");
  // Create a new X509TrustManager
  sslContext.init(null, getTrustManager(), null);
} catch (NoSuchAlgorithmException | KeyManagementException e) {
  throw e;
}
final Client client = ClientBuilder.newBuilder().hostnameVerifier((s, session) -> true)
  .sslContext(sslContext).build();
return client;

private TrustManager[] getTrustManager() {
  return new TrustManager[] {
    new X509TrustManager() {
      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }
      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType)
      throws CertificateException {
      }
      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType)
      throws CertificateException {
      }
    }
  };
}
