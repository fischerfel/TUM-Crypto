    final KeyStore trustStore = KeyStore.getInstance("BKS");
    final InputStream in = context.getResources().openRawResource(
            R.raw.geotrust_cert);
    trustStore.load(in, null);

    final TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);

    final SSLContext sslCtx = SSLContext.getInstance("TLS");
    sslCtx.init(null, tmf.getTrustManagers(),
            new java.security.SecureRandom());

    HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx
            .getSocketFactory());
