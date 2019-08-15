    String type = KeyStore.getDefaultType();
    KeyStore trustStore = KeyStore.getInstance(type);
    trustStore.load(null, null);
    trustStore.setCertificateEntry("ca", new CA().getCert());

    String tmfAlg = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlg);
    tmf.init(trustStore);

    try {
         SSLContext context = SSLContext.getInstance("TLS");
         context.init(null, tmf.getTrustManagers(), null);
         SSLEngine engine = context.createSSLEngine();

         AsyncSSLSocketWrapper.handshake(socketNormal, url, port, engine, tmf.getTrustManagers(), new NoopHostnameVerifier(), true, (e, socket1) -> {
// ... more 
