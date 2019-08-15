public SSLContext getSslContext(String keyStorePath, String keyStoreType, String trustStorePath) {
  KeyStore keyStore = KeyStore.getInstance(keyStoreType);
  InputStream ksis = ClassLoader.getSystemResourceAsStream(keyStorePath);
  keyStore.load(ksis, "mypassword".toCharArray());
  ksis.close();

  KeyStore trustStore = KeyStore.getInstance("JKS");
  InputStream tsis = ClassLoader.getSystemResourceAsStream(trustStorePath);
  trustStore.load(tsis, "mypassword".toCharArray());
  tsis.close();

  TrustManagerFactory tmf =
      TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
  tmf.init(trustStore);

  KeyManagerFactory kmf =
      KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
  kmf.init(keyStore, "mypassword".toCharArray());

  sslContext = SSLContext.getInstance("TLS");
  sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
  return sslContext;
}
