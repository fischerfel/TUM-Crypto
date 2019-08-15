private static SSLSocketFactory trustStoreWithClientPCKSAndServerCert() throws Exception {

    KeyStore trustStore = KeyStore.getInstance("jceks");
    InputStream inputStream = null;
    try {
        inputStream = new FileInputStream("ts-stage.jks");
        trustStore.load(inputStream, TRUST_STORE_PASSWD
                .toCharArray());
    } finally {
        inputStream.close();
    }

    final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(trustStore, PKCS_PASSWD.toCharArray());

    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());
    return sslContext.getSocketFactory();
}
