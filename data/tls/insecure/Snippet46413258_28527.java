    KeyStore keystore = KeyStore.getInstance("JKS");
    InputStream in = getClass().getResourceAsStream("/mytruststore.jks");
    keystore.load(in, "password".toCharArray());
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keystore);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, trustManagers, null);

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
