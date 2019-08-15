    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(getKeyManagers(), null, null);

    Undertow.builder().addHttpsListener(10443, "0.0.0.0", sslContext)...
