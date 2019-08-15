public void afterPropertiesSet() throws Exception {
    Assert.isTrue(StringUtils.isNotBlank(getKeystoreName()), "Key Store Name is Blank");
    Assert.isTrue(StringUtils.isNotBlank(getKeystorePassword()), "Key Store Password is Blank.");
    Assert.isTrue(StringUtils.isNotBlank(getKeystorePath()), "Key Store Path is Blank");
    Assert.isTrue(StringUtils.isNotBlank(getTruststoreName()), "Trust Store Name is Blank");
    Assert.isTrue(StringUtils.isNotBlank(getTruststorePassword()), "Trust Store Password is Blank.");
    Assert.isTrue(StringUtils.isNotBlank(getTruststorePath()), "Trust Store Path is Blank");

    if (getHttpClient() == null) {
        // Initialize HTTP Client
        initializeHttpClient();
    }

    Assert.notNull(getHttpClient(), "HTTP Client is NULL after initialization");
}

public ClientRequest createClientRequest(String uri) throws URISyntaxException {
    ClientExecutor clientExecutor = new ApacheHttpClient4Executor(getHttpClient());
    ClientRequestFactory fac = new ClientRequestFactory(clientExecutor, new URI(uri));
    return fac.createRequest(uri);
}

/**
 * Initializes the HTTP Client
 * 
 * @throws KeyStoreException
 * @throws NoSuchAlgorithmException
 * @throws UnrecoverableKeyException
 * @throws KeyManagementException
 */
private void initializeHttpClient() throws Exception {

    if (isCheckPeerCertificates()) {
        checkPeerCerts();
    }

    // Create Trust and Key Managers
    // Use TrustManager and KeyManager instead of KeyStore
    TrustManager[] trustManagers = getTrustManagers(getTruststorePassword());
    KeyManager[] keyManagers = getKeyManagers(getKeystorePassword());

    // Create SSL Context
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(keyManagers, trustManagers, new SecureRandom());

    // Create SSL Factory
    SSLSocketFactory sslSocketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    // Register https and http with scheme registry
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme(HTTP, 80, PlainSocketFactory.getSocketFactory()));
    schemeRegistry.register(new Scheme(HTTPS, 443, sslSocketFactory));

    // Set connection params
    HttpConnectionParams.setConnectionTimeout(httpParameters, serviceConnectionTimeout);
    HttpConnectionParams.setSoTimeout(httpParameters, readTimeout);
    HttpConnectionParams.setStaleCheckingEnabled(httpParameters, true);

    // Create Connection Manager
    PoolingClientConnectionManager clientManager = new PoolingClientConnectionManager(schemeRegistry);
    clientManager.setMaxTotal(maxTotalConnections);
    clientManager.setDefaultMaxPerRoute(defaultMaxConnectionsPerHost);

    httpClient = new DefaultHttpClient(clientManager, httpParameters);
}

private TrustManager[] getTrustManagers(String trustStorePassword) throws Exception {
    String truststoreFilePath = getTruststorePath() + getTruststoreName();
    InputStream trustStoreInput = getClass().getClassLoader().getResourceAsStream(truststoreFilePath);
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(trustStoreInput, trustStorePassword.toCharArray());
    TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmfactory.init(trustStore);
    return tmfactory.getTrustManagers();
}

private KeyManager[] getKeyManagers(String keyStorePassword) throws Exception {
    String keystoreFilePath = getKeystorePath() + getKeystoreName();
    InputStream keyStoreInput = getClass().getClassLoader().getResourceAsStream(keystoreFilePath);
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(keyStoreInput, keyStorePassword.toCharArray());
    KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmfactory.init(keyStore, keyStorePassword.toCharArray());
    return kmfactory.getKeyManagers();
}
