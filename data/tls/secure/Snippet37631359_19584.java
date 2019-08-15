private SSLContext createSSLContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException, NoSuchProviderException {
    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(new FileInputStream("c:\\tmp\\clientkeystore.jks"), "123456".toCharArray());

    // Create key manager
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    keyManagerFactory.init(keyStore, "123456".toCharArray());
    KeyManager[] km = keyManagerFactory.getKeyManagers();

    // Create trust manager
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
    trustManagerFactory.init(keyStore);
    TrustManager[] tm = trustManagerFactory.getTrustManagers();

    // Initialize SSLContext
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(km, tm, null);

    return sslContext;
}
