    // load your key store as a stream and initialize a KeyStore
    InputStream trustStream = new FileInputStream("Resources/keystore.ImportKey");
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

    // if your store is password protected then declare it (it can be null however)
    String trustPassword = "changeit";

    // load the stream to your store
    trustStore.load(trustStream, trustPassword.toCharArray());

    // initialize a trust manager factory with the trusted store
    TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustFactory.init(trustStore);

    KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyFactory.init(trustStore, trustPassword.toCharArray());

    // get the trust managers from the factory
    TrustManager[] trustManagers = trustFactory.getTrustManagers();
    KeyManager[] keyManagers = keyFactory.getKeyManagers();

    // initialize an ssl context to use these managers and set as default
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(keyManagers, trustManagers, null);
    SSLContext.setDefault(sslContext);
