  HttpsServer server = HttpsServer.create(new InetSocketAddress(secureConnection.getPort()), 0);
  SSLContext sslContext = SSLContext.getInstance("TLS");

  sslContext.init(secureConnection.getKeyManager().getKeyManagers(), secureConnection.getTrustManager().getTrustManagers(), null);

  server.setHttpsConfigurator(new SecureServerConfiguration(sslContext));
  server.createContext("/", new RootHandler());
  server.createContext("/test", new TestHandler());
  server.setExecutor(Executors.newCachedThreadPool());
  server.start();
