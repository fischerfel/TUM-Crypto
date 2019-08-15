  public SSLSocketFactory generateMqSSLSocketFactory() throws Exception {
    KeyStore keyStore = KeystoreGenerator.generateKeystore(new KeystoreGenerator.PrivateKeyCertificateEntry(
        getClass().getResourceAsStream("/keys/mq-client-key.pkcs8"),
        getClass().getResourceAsStream("/keys/mq-client-certificate.pem"),
        "mq_client", "changeit".toCharArray()
    ));

    // Generate keystore to authorize client on server
    KeyStore trustStore = KeystoreGenerator.generateTrustStore(new KeystoreGenerator.CertificateEntry(
        getClass().getResourceAsStream("/keys/mq-server-certificate.pem"), "mq_server"));

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, "changeit".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    return sslContext.getSocketFactory();
  }
