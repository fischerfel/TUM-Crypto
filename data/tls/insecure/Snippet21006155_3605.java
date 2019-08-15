private SSLSocketFactory createSSLFactory() {
  KeyStore keyStore = null;
  TrustManagerFactory tmf = null;
  SSLContext ctx = null;

  try {
    keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    InputStream is = null;
    is = SSLConnection.class.getResourceAsStream("/" + "my-keystore");
    keyStore.load(is, "changeit".toCharArray());
    tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keyStore);
    ctx = SSLContext.getInstance("TLSv1");
    ctx.init(null, tmf.getTrustManagers(), null);
    SSLSocketFactory factory = ctx.getSocketFactory();
    return factory;
  } catch (Exception e) {
    // exception handling
  }
  return null;
}
