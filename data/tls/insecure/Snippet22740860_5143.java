Endpoint endpoint = Endpoint.create(new TicketQueryImpl());
        javax.net.ssl.SSLContext ssl = javax.net.ssl.SSLContext.getInstance("TLS");
        javax.net.ssl.KeyManagerFactory keyFactory = javax.net.ssl.KeyManagerFactory
                .getInstance(javax.net.ssl.KeyManagerFactory.getDefaultAlgorithm());
        KeyStore store = KeyStore.getInstance("JKS");

        store.load(new FileInputStream("c:/thekey.txt"), "password".toCharArray());
        keyFactory.init(store, "password".toCharArray());

        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                .getDefaultAlgorithm());
        trustFactory.init(store);
        ssl.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), new SecureRandom());
        HttpsConfigurator configurator = new HttpsConfigurator(ssl);
        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress("localhost", 443), 443);
        httpsServer.setHttpsConfigurator(configurator);
        HttpContext httpContext = httpsServer.createContext("/127.0.0.1:443/");
        httpsServer.start();

        endpoint.publish(httpContext);
