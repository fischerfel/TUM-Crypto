    OkHttpClient client     = new OkHttpClient();
    KeyStore keyStoreClient = getClientKeyStore();
    KeyStore keyStoreServer = getServerKeyStore();
    String algorithm        = ALGO_DEFAULT;//this is defined as "PKIX"

    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
    keyManagerFactory.init(keyStoreClient, PASSWORD_SERVER.toCharArray());

    SSLContext sslContext = SSLContext.getInstance("TLS");

    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
    trustManagerFactory.init(keyStoreServer);

    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

    client.setSslSocketFactory(sslContext.getSocketFactory());
    client.setConnectTimeout(32, TimeUnit.SECONDS); // connect timeout
    client.setReadTimeout(32, TimeUnit.SECONDS);    // socket timeout

    return client;
