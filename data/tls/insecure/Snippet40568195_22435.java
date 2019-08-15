  public void initHttpsServer(int port, String certificateFilePath, String sslPassword)
  {
    try
    {
      this.server = HttpsServer.create(new InetSocketAddress(port), 0);
      SSLContext sslContext = SSLContext.getInstance("TLS");
      char[] password = sslPassword.toCharArray();
      KeyStore ks = KeyStore.getInstance("JKS");
      FileInputStream fis = new FileInputStream(certificateFilePath);
      ks.load(fis, password);
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, password);
      TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
      tmf.init(ks);
      sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
      this.server.setHttpsConfigurator(new HttpsConfigurator(sslContext)
      {
        @Override
        public void configure(HttpsParameters params)
        {
          try
          {
            SSLContext c = SSLContext.getDefault();
            SSLEngine engine = c.createSSLEngine();
            params.setNeedClientAuth(false);
            params.setCipherSuites(engine.getEnabledCipherSuites());
            params.setProtocols(engine.getEnabledProtocols());
            SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
            params.setSSLParameters(defaultSSLParameters);
          }
