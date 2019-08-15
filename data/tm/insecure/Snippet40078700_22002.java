   TrustManager[] certs = new TrustManager[]{
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

  public static class TrustAllHostNameVerifier implements HostnameVerifier {

      public boolean verify(String hostname, SSLSession session) {
          return true;
      }

  }
  private Client getWebClient(AppConfiguration configuration, Environment env) {
      SSLContext ctx = SSLContext.getInstance("SSL");
      ctx.init(null, certs, new SecureRandom());
      Client client = new JerseyClientBuilder(env)
          .using(configuration.getJerseyClient())
          .sslContext(ctx)
          .build("MyClient");
      return client;
  }
