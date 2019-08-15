  protected void setSSLSocketFactory(final String agenda, final BindingProvider service) {
    service.getRequestContext().put(JAXWSProperties.SSL_SOCKET_FACTORY,
        MutualSSLContext.getFactory(configuration, agenda));
  }

  public static final SSLSocketFactory getFactory(final RobaktConfiguration cfg, final String agenda) {

if (cfg == null || agenda == null) {
  return null;
}

  try {
    final File keyStore = cfg.getKeystorePath(agenda);
    final KeyManager[] keyManagers;
    if (keyStore != null) {
      keyManagers = new KeyManager[] {new MutualKeyManager(cfg, agenda)};
    } else {
      keyManagers = null;
    }
    final SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(keyManagers, new TrustManager[] {new MutualTrustManager()}, null);
    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

    return sslSocketFactory;
  } catch (Exception e) {
    throw new MutualSSLException(e.getMessage(), e);
  }
