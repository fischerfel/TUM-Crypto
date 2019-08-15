    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    FileInputStream finStream = new FileInputStream(certFile.toFile());
    X509Certificate x509Certificate = (X509Certificate)cf.generateCertificate(finStream);

    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null);
    keyStore.setCertificateEntry("someAlias", x509Certificate);

    TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    instance.init(keyStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, instance.getTrustManagers(), null);
    SSLContext.setDefault(sslContext);

    Undertow.Builder builder = Undertow.builder();
    builder.addHttpsListener(httpsPort, ipAddress, sslContext);
