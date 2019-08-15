final SSLContext context = SSLContext.getInstance("TLS");
final KeyStore keystore = KeyStore.getInstance("pkcs12");
keystore.load(new FileInputStream(new File("ca-cli.pkcs12")), "password".toCharArray());
final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keystore, "password".toCharArray());
context.init(keyManagerFactory.getKeyManagers(), new TrustManager[] {
  new X509TrustManager() {
    @Override
    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
    @Override
    public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
      // TODO Auto-generated method stub
    }
    @Override
    public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
      // TODO Auto-generated method stub
    }
  }
}, new SecureRandom());
