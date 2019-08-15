 try {
    final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
               @Override
               public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

               @Override
               public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

               @Override
               public java.security.cert.X509Certificate[]getAcceptedIssuers() {
               return new java.security.cert.X509Certificate[]{};
               }
        }
};
final SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
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
