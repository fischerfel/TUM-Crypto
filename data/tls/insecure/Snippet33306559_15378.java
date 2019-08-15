  KeyStore keyStore = KeyStore.getInstance("JKS");
  keyStore.load(new FileInputStream("test.jks"),"passphrase".toCharArray());

  // Create key manager
  KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
  keyManagerFactory.init(keyStore, "passphrase".toCharArray());
  KeyManager[] km = keyManagerFactory.getKeyManagers();

  // Create trust manager
  TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
  trustManagerFactory.init(keyStore);
  TrustManager[] tm = trustManagerFactory.getTrustManagers();

  // Initialize SSLContext
  SSLContext sslContext = SSLContext.getInstance("TLSv1");
  sslContext.init(km,  tm, null);
