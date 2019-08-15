private OkHttpClient getClient() {
    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.sslSocketFactory(getPinnedCertSslSocketFactory());
    return lBuilder.build();
  }



  private SSLSocketFactory getPinnedCertSslSocketFactory() {
    try {
      KeyStore trusted = KeyStore.getInstance("BKS");
      File trustStoreFile = new File(...);
      InputStream in = new FileInputStream(trustStoreFile);
      trusted.load(in, TRUSTSTORE_PW.toCharArray());
      SSLContext sslContext = SSLContext.getInstance("TLS");
      TrustManagerFactory trustManagerFactory = TrustManagerFactory
          .getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(trusted);
      sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
      return sslContext.getSocketFactory();
    } catch (Exception e) {
      LOGGER.error("getPinnedCertSslSocketFactory", e);
    }
    return null;
  }

public void doRequest() {
     OkHttpClient client = getClient();
     Request request = new Request.Builder().url(URL).post(BODY).build();
     client.newCall(request).execute();
}
