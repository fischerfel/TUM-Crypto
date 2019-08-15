// First initialize the key and trust material.
    KeyStore ksKeys = KeyStore.getInstance("JKS");
    ksKeys.load(new FileInputStream("/.../myKey"), passphrase);
    KeyStore ksTrust = KeyStore.getInstance("JKS");
    ksTrust.load(new FileInputStream("/../myCertificate"), passphrase);

    sslContext = SSLContext.getInstance("TLS");
    sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    // We're ready for the engine.
    SSLEngine engine = sslContext.createSSLengine(hostname, port);

    // Use as client
    engine.setUseClientMode(true);
