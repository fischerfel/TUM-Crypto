    System.setProperty("https.protocols", "TLSv1.2");
    System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
    KeyStore keyStore = getKeyStore();

    KeyManagerFactory keyManagerFactory;
    keyManagerFactory = KeyManagerFactory.getInstance("PKIX");
    keyManagerFactory.init(keyStore, getPassword());

    KeyStore trustStore = getTrustStore();
    TrustManagerFactory trustManagerFactory;
    trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
    trustManagerFactory.init(trustStore);

    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

    SSLSocket socket = (SSLSocket) sslContext.getSocketFactory().createSocket(
    ipAddress, port);
    socket.setTcpNoDelay(true);
    socket.setKeepAlive(false);
    socket.setNeedClientAuth(true);
    socket.startHandshake();
