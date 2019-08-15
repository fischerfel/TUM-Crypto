        Endpoint endpoint = Endpoint.create(new UserWs());
        //UserWs would be a WebService.
        SSLContext ssl = SSLContext.getInstance("SSLv3");

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore jks = KeyStore.getInstance("PKCS12");

        jks.load(new FileInputStream(path), pwField.getText().toCharArray());

        kmf.init(jks, pwField.getText().toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();

        tmf.init(jks);

        TrustManager[] trustManagers = tmf.getTrustManagers();

        ssl.init(keyManagers, trustManagers, new SecureRandom());

        HttpsConfigurator configurator = new HttpsConfigurator(ssl);

        server = HttpsServer.create(new InetSocketAddress("localhost", 443), 443);
        server.setHttpsConfigurator(configurator);

        HttpContext context = server.createContext("/ws");
        server.start();

        endpoint.publish(context);
