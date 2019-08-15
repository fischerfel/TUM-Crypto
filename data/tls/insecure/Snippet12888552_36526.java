    String passphrase = "secret"
    KeyStore keyStore;
    TrustManagerFactory tmf;
    KeyManagerFactory kmf;
    SSLContext sslContext;
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextInt();

        keyStore = KeyStore.getInstance("JKS");
        keyStore.load(this.getClass().getClassLoader().getResourceAsStream("server.private"),passphrase.toCharArray());
        tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, passphrase.toCharArray());
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);
        SSLServerSocketFactory sslserversocketfactory = sslContext.getServerSocketFactory();
        SSLServerSocket sslserversocket =
        (SSLServerSocket)sslserversocketfactory.createServerSocket(9999);
