        FileInputStream stream = new FileInputStream("mystore.keystore.jks");
        char[] serverKeyStorePassword = "yourcode".toCharArray();
        KeyStore serverKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        serverKeyStore.load(stream, serverKeyStorePassword);

        String kmfAlgoritm = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(kmfAlgoritm);
        kmf.init(serverKeyStore, serverKeyStorePassword);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(kmfAlgoritm);
        trustManagerFactory.init(serverKeyStore);

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        SSLSocketFactory sf = sslContext.getSocketFactory();
        mockWebServer.useHttps(sf, false);
