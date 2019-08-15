public class SslOptionsFactoryBean extends AbstractFactoryBean<SSLOptions> {

    private Resource keyStore;
    private String keyStorePassword;
    private Resource trustStore;
    private String trustStorePassword;

    @Override
    public Class<?> getObjectType() {
        return SSLOptions.class;
    }

    @Override
    protected SSLOptions createInstance() throws Exception {

        KeyManager[] keyManagers = getKeyStore() != null
                ? createKeyManagerFactory(getKeyStore(), getKeyStorePassword()).getKeyManagers() : null;

        TrustManager[] trustManagers = getTrustStore() != null
                ? createTrustManagerFactory(getTrustStore(), getTrustStorePassword()).getTrustManagers() : null;

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);

        return new SSLOptions(sslContext, SSLOptions.DEFAULT_SSL_CIPHER_SUITES);
    }

    private static KeyManagerFactory createKeyManagerFactory(Resource keystoreFile, String storePassword)
            throws GeneralSecurityException, IOException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        try (InputStream inputStream = keystoreFile.getInputStream()) {
            keyStore.load(inputStream, StringUtils.hasText(storePassword) ? storePassword.toCharArray() : null);
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, StringUtils.hasText(storePassword) ? storePassword.toCharArray() : new char[0]);

        return keyManagerFactory;
    }

    private static TrustManagerFactory createTrustManagerFactory(Resource trustFile, String storePassword)
            throws GeneralSecurityException, IOException {

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

        try (InputStream inputStream = trustFile.getInputStream()) {
            trustStore.load(inputStream, StringUtils.hasText(storePassword) ? storePassword.toCharArray() : null);
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        return trustManagerFactory;
    }

    public Resource getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(Resource keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public Resource getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(Resource trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }
}
