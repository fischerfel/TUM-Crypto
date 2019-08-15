SSLContext ssl = SSLContext.getInstance("TLS");

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()); 
KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
store.load(new FileInputStream(keystoreFile), keyPass.toCharArray()); 

kmf.init(store, keyPass.toCharArray());
KeyManager[] keyManagers = new KeyManager[1];
keyManagers = kmf.getKeyManagers();

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(store);
TrustManager[] trustManagers = tmf.getTrustManagers();
ssl.init(keyManagers, trustManagers, new SecureRandom());

HttpsConfigurator configurator = new HttpsConfigurator(ssl);
HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress("localhost", 8443), 8443);
httpsServer.setHttpsConfigurator(configurator);   

HttpContext context = httpsServer.createContext("/test");
httpsServer.start();

endpoint.publish(context);
