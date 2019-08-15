    tmf.init(trustKeyStore);
    kmf.init(keyStore, password);

    // create, init SSLContext
    SSLContext sslCtx = SSLContext.getInstance("TLS");
    sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    // create SSLSocketFactory and SSLSocket
    SSLServerSocketFactory sslSocketFactory =   (SSLServerSocketFactory)sslCtx.getServerSocketFactory();
    sslSocket = (SSLServerSocket)sslSocketFactory.createServerSocket(1234);
