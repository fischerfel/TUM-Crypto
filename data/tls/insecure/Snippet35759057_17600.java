    KeyStore keystore; // Get your own keystore here
    SSLContext sslContext = SSLContext.getInstance("TLS");
    TrustManager[] tm = CompositeX509TrustManager.getTrustManagers(keystore);
    sslContext.init(null, tm, null);
