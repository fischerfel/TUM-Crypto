    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(Resources.getResource("trust.keystore").openStream(),
        "changeit".toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);
    SSLContext sslCtx = SSLContext.getInstance("TLSv1.2");
    sslCtx.init(null, tmf.getTrustManagers(), null);
    socketFactory = sslCtx.getSocketFactory();
    Socket s = socketFactory.createSocket("localhost", 4443);
    ((SSLSocket)s).setEnabledCipherSuites(new String[]{
        "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256"
    });
    ((SSLSocket)s).setEnabledProtocols(new String[]{
        "TLSv1.2"
    });
    Writer writer = new BufferedWriter(
        new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
    writer.write("HELLO WORLD\n");
    writer.flush();
