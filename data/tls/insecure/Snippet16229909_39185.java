@Provides @Singleton
SSLContext provideSSLContext(KeyStore keystore, char[] password) {
  String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
  X509KeyManager customKeyManager = getKeyManager("SunX509", keystore, password);
  X509KeyManager jvmKeyManager = getKeyManager(defaultAlgorithm, null, null);
  X509TrustManager customTrustManager = getTrustManager("SunX509", keystore);
  X509TrustManager jvmTrustManager = getTrustManager(defaultAlgorithm, null);

  KeyManager[] keyManagers = { new CompositeX509KeyManager(ImmutableList.of(jvmKeyManager, customKeyManager)) };
  TrustManager[] trustManagers = { new CompositeX509TrustManager(ImmutableList.of(jvmTrustManager, customTrustManager)) };

  SSLContext context = SSLContext.getInstance("SSL");
  context.init(keyManagers, trustManagers, null);
  return context;
}

private X509KeyManager getKeyManager(String algorithm, KeyStore keystore, char[] password) {
  KeyManagerFactory factory = KeyManagerFactory.getInstance(algorithm);
  factory.init(keystore, password);
  return Iterables.getFirst(Iterables.filter(
      Arrays.asList(factory.getKeyManagers()), X509KeyManager.class), null);
}

private X509TrustManager getTrustManager(String algorithm, KeyStore keystore) {
  TrustManagerFactory factory = TrustManagerFactory.getInstance(algorithm);
  factory.init(keystore);
  return Iterables.getFirst(Iterables.filter(
      Arrays.asList(factory.getTrustManagers()), X509TrustManager.class), null); 
}
