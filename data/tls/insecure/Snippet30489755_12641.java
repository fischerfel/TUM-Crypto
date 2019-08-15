/**
 * @param keyStorePath Path to keystore, if absent is not used.
 * @param trustStorePath Path to truststore, if absent is not used.
 * @return {@link com.datastax.driver.core.SSLOptions} with the given keystore and truststore path's for
 * server certificate validation and client certificate authentication.
 */
public SSLOptions getSSLOptions(Optional<String> keyStorePath, Optional<String> trustStorePath) throws Exception {

    TrustManagerFactory tmf = null;
    if(trustStorePath.isPresent()) {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(this.getClass().getResourceAsStream(trustStorePath.get()), DEFAULT_CLIENT_TRUSTSTORE_PASSWORD.toCharArray());

        tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
    }

    KeyManagerFactory kmf = null;
    if(keyStorePath.isPresent()) {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(this.getClass().getResourceAsStream(keyStorePath.get()), DEFAULT_CLIENT_KEYSTORE_PASSWORD.toCharArray());

        kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, DEFAULT_CLIENT_KEYSTORE_PASSWORD.toCharArray());
    }

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf != null ? kmf.getKeyManagers() : null, tmf != null ? tmf.getTrustManagers() : null, new SecureRandom());

    return new SSLOptions(sslContext, SSLOptions.DEFAULT_SSL_CIPHER_SUITES);
}
