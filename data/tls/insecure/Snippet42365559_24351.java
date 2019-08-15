KeyStore ks = KeyStore.getInstance("JKS");
// Load KeyStore into "ks"

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, jksPassword.toCharArray());

TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(ks);

SSLContext sc = SSLContext.getInstance("TLS");
sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

HttpsServer hsS = HttpsServer.create(sBind, 0);
hsS.setHttpsConfigurator(new HttpsConfigurator(sc) {
  @Override
  public void configure(HttpsParameters p) {
    try {
      SSLContext c = getSSLContext();
      SSLEngine e = c.createSSLEngine();

      p.setNeedClientAuth(false);
      p.setProtocols(new String[] { "TLSv1.1" });
      p.setCipherSuites(e.getEnabledCipherSuites());
      p.setSSLParameters(c.getDefaultSSLParameters());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
});
