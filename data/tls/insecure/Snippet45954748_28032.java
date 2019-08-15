@Bean
public WSClient wsClient(Jaxb2Marshaller marshaller) throws Exception {
    WSClient client = new WSClient();
    client.setDefaultUri(getClientUrl());
    client.setMarshaller(marshaller);
    client.setUnmarshaller(marshaller);

    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(null);
    createKeyStoreFromResource(ks, keyStore);

    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(ks, null);

    KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
    ts.load(null);
    createKeyStoreFromResource(ts, trustStore);

    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(ts);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

    HttpsUrlConnectionMessageSender messageSender = new HttpsUrlConnectionMessageSender();
    messageSender.setKeyManagers(keyManagerFactory.getKeyManagers());
    messageSender.setTrustManagers(trustManagerFactory.getTrustManagers());
    messageSender.setSslSocketFactory(sslContext.getSocketFactory());
    messageSender.setSslProtocol(sslContext.getProtocol());
    messageSender.setSslProvider(sslContext.getProvider().getName());

    messageSender.setHostnameVerifier((hostname, sslSession) -> true);

    client.setMessageSender(messageSender);
    return client;
}

private void createKeyStoreFromResource(KeyStore ks, Resource resource)
    throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    BufferedInputStream bis = new BufferedInputStream(resource.getInputStream());

    while(bis.available() > 0) {
        Certificate certificate = cf.generateCertificate(bis);
        ks.setCertificateEntry("fiddler" + bis.available(), certificate);
    }
}
