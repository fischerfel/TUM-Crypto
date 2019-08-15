    KeyStore trustStore = KeyStore.getInstance("BKS");
    BufferedInputStream is = new BufferedInputStream(c.getAssets().open("key.bks"));
    trustStore.load(is, "12345".toCharArray());
    Log.i("Cert", "ca " + (new CA().getCert()).getSubjectDN());

    String tmfAlg = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlg);
    tmf.init(trustStore);

    try {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        SSLEngine engine = context.createSSLEngine();

        AsyncSSLSocketWrapper.handshake(socketNormal, url, port,
        engine, tmf.getTrustManagers(), new NoopHostnameVerifier(), true, (e, socket1) -> {
