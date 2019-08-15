SSLContext ssl = SSLContext.getInstance("TLS");

KeyManagerFactory keyFactory = KeyManagerFactory                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
KeyStore store = KeyStore.getInstance("JKS");

store.load(new FileInputStream(keystoreFile),keyPass.toCharArray());

keyFactory.init(store, keyPass.toCharArray());


TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

trustFactory.init(store);

ssl.init(keyFactory.getKeyManagers(),
trustFactory.getTrustManagers(), new SecureRandom());

HttpsConfigurator configurator = new HttpsConfigurator(ssl);

HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(hostname, port), port);

httpsServer.setHttpsConfigurator(configurator);

HttpContext httpContext = httpsServer.createContext(uri);

httpsServer.start();

endpoint.publish(httpContext);
