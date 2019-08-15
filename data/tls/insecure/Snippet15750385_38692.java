  KeyStore ebKeyStore = KeyStore.getInstance("PKCS12");
  try (InputStream clientCertKeyInput = new FileInputStream(pfxFilePath)) {
      ebKeyStore.load(clientCertKeyInput, passwdChars);
  }
  KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
  keyManagerFactory.init(ebKeyStore, passwdChars);

  SSLContext sslCtx = SSLContext.getInstance("TLS");
  sslCtx.init(keyManagerFactory.getKeyManagers(), 
              null, // default javax.net.ssl.trustStore
              new SecureRandom());

  sslSocketFactory = sslCtx.getSocketFactory();
