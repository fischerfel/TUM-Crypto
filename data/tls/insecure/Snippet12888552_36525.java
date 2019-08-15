    KeyStore keyStore;
    TrustManagerFactory tmf;
    KeyManagerFactory kmf;
    SSLContext sslContext;
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextInt();

        keyStore = KeyStore.getInstance("JKS");
        keyStore.load(this.getClass().getClassLoader().getResourceAsStream("server.public"),"public".toCharArray());
        tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, "public".toCharArray());
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);
        SSLSocketFactory sslsocketfactory = sslContext.getSocketFactory();
        SSLSocket sslsocket = (SSLSocket)sslsocketfactory.createSocket("localhost", 9999);
