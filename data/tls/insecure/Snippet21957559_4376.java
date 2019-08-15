  OkHttpClient okHttpClient = new OkHttpClient();
  SSLContext sslContext;
  try {
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, null, null);
  } catch (GeneralSecurityException e) {
    throw new AssertionError(); // The system has no TLS. Just give up.
  }
  okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
