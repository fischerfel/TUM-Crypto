    keystore.load(getResources().openRawResource(R.raw.temafon),
                "W0d3Uoa5PkED".toCharArray());
    final TrustManager trustManager = new TemafonTrustManager(keystore);

    final SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[] { trustManager }, null);

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
            .getSocketFactory());
