public static SSLContext serverSslContext(String password1, String password2, String password3) {
    try {
        KeyStore keyStore = loadKeyStore("my-keystore.jks", password1);
        KeyStore trustStore = loadKeyStore("my-truststore.ts", password3);
        return createSSLContext(keyStore, trustStore, password2);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

private static SSLContext createSSLContext(final KeyStore keyStore, final KeyStore trustStore, String keyStorePassword) throws IOException {
    try {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        return sslContext;
    } catch (Exception e) {
        throw new IOException("Unable to create and initialise the SSLContext", e);
    }
}

private static KeyStore loadKeyStore(final String name, String password) throws IOException {
    try(InputStream stream = new FileInputStream(name)) {
        KeyStore loadedKeystore = KeyStore.getInstance("JKS");
        loadedKeystore.load(stream, password.toCharArray());
        return loadedKeystore;
    } catch (Exception e) {
        throw new IOException(String.format("Unable to load KeyStore %s", name), e);
    }
}
