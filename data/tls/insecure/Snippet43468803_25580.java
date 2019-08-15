public HttpsConnectionContext useHttps(ActorSystem system) {
HttpsConnectionContext https = null;
try {
  final char[] password = properties.keystorePassword().toCharArray();

  final KeyStore ks = KeyStore.getInstance("PKCS12");
  final InputStream keystore = WDService.class.getClassLoader().getResourceAsStream("wtkeystore.p12");
  if (keystore == null) {
    throw new RuntimeException("Keystore required!");
  }
  ks.load(keystore, password);
  final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
  keyManagerFactory.init(ks, password);

  final TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
  tmf.init(ks);

  final SSLContext sslContext = SSLContext.getInstance("TLS");
  sslContext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
  final AkkaSSLConfig sslConfig = AkkaSSLConfig.get(system);
  https = ConnectionContext.https(sslContext);
} catch (NoSuchAlgorithmException | KeyManagementException e) {
  system.log().error(e.getCause() + " while configuring HTTPS.", e);
} catch (CertificateException | KeyStoreException | UnrecoverableKeyException | IOException e) {
  system.log().error(e.getCause() + " while ", e);
}

return https;
