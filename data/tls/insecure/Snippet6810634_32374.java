  SSLContext ssl = SSLContext.getInstance("SSLv3");
  KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
  KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
  String password = Configuration.getConfig("keyStorePassword");
  store.load(new FileInputStream(new File(Configuration.getConfig("keyStore"))), password.toCharArray());
  kmf.init(store, password.toCharArray());
  KeyManager[] keyManagers = new KeyManager[1];
  keyManagers = kmf.getKeyManagers();
  TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
  tmf.init(store);
  TrustManager[] trustManagers = tmf.getTrustManagers();
  ssl.init(keyManagers, trustManagers, new SecureRandom());

  HttpsConfigurator configurator = new HttpsConfigurator(ssl);
  Integer port = Integer.parseInt(Configuration.getConfig("port"));
  HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(Configuration.getConfig("host"), port), 0);
  httpsServer.setHttpsConfigurator(configurator);

  Implementor implementor = new Implementor(); // class with @WebService etc.
  HttpContext context = (HttpContext) httpsServer.createContext("/EventWebService");
  Endpoint endpoint = Endpoint.create( implementor );
  endpoint.publish(context);
