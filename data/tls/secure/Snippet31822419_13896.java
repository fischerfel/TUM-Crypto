    System.setProperty("https.protocols", "TLSv1.2");
    System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

    KeyManagerFactory keyManagerFactory;
    keyManagerFactory = KeyManagerFactory.getInstance("PKIX");
    keyManagerFactory.init(keyStore, keyPairPassword);

    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
    trustManagerFactory.init(trustStore);

    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

    SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

    SSLServerSocket sslServerSocket
            = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
    sslServerSocket.setEnabledCipherSuites(new String[]{"TLS_DHE_RSA_WITH_AES_128_CBC_SHA256",
    "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256"});
    sslServerSocket.setNeedClientAuth(true);
