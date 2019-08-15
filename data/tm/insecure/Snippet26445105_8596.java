static OkHttpClient getUnsafeOkHttpClient() {
  try {
      // Create a trust manager that does not validate certificate chains
      final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
          @Override
          public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                  String authType) throws CertificateException {
          }

          @Override
          public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                  String authType) throws CertificateException {
          }

          @Override
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return null;
          }
      } };

      // Install the all-trusting trust manager
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      // Create an ssl socket factory with our all-trusting manager
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      OkHttpClient okHttpClient = new OkHttpClient();
      okHttpClient.setSslSocketFactory(sslSocketFactory);
      okHttpClient.setHostnameVerifier(new HostnameVerifier() {
          @Override
          public boolean verify(String hostname, SSLSession session) {
              return true;
          }
      });

      return okHttpClient;
  } catch (Exception e) {
      throw new RuntimeException(e);
  }
}
