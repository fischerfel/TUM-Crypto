// Stitch it all together with HttpClient
CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(getSSLSocketFactory()).build();


private SSLConnectionSocketFactory getSSLSocketFactory() {
    try {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        KeyManager[] keyManager = getKeyManager("pkcs12", "path/to/cert.p12"), "p12_password"));
        TrustManager[] trustManager = getTrustManager("jks", "path/to/CA.truststore", "trust_store_password"));
        sslContext.init(keyManager, trustManager, new SecureRandom());

        return new SSLConnectionSocketFactory(sslContext);
    } catch (Exception e) {
        throw new RuntimeException("Unable to setup keystore and truststore", e);
    }
}

/**
 * Some useful commands for looking at the client certificate and private key:
 * keytool -keystore certificate.p12 -list -storetype pkcs12 -v
 * openssl pkcs12 -info -in certificate.p12
 */
private KeyManager[] getKeyManager(String keyStoreType, String keyStoreFile, String keyStorePassword) throws Exception {
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyStore.load(this.getClass().getClassLoader().getResourceAsStream(keyStoreFile), keyStorePassword.toCharArray());
    kmf.init(keyStore, keyStorePassword.toCharArray());

    return kmf.getKeyManagers();
}

/**
 * Depending on what format (pem / cer / p12) you have received the CA in, you will need to use a combination of openssl and keytool
 * to convert it to JKS format in order to be loaded into the truststore using the method below. 
 *
 * You could of course use keytool to import this into the JREs TrustStore - my situation mandated I create it on the fly.
 *
 * Useful command to look at the CA certificate:
 * keytool -keystore root_ca.truststore -list -storetype jks -v
 *
 */
private TrustManager[] getTrustManager(String trustStoreType, String trustStoreFile, String trustStorePassword) throws Exception {
    KeyStore trustStore = KeyStore.getInstance(trustStoreType);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustStore.load(this.getClass().getClassLoader().getResourceAsStream(trustStoreFile), trustStorePassword.toCharArray());
    tmf.init(trustStore);

    return tmf.getTrustManagers();
}
