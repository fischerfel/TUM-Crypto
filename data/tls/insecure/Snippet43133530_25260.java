static SSLContext createSSLContext() throws Exception {
    final KeyStore ks = createSelfSignedKeyStore();
    final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, new char[0]);

    final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);

    final SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    return sslContext;
}

private static KeyStore createSelfSignedKeyStore() throws Exception {
    final KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(null);

    final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(1024);
    final KeyPair keyPair = keyGen.generateKeyPair();
    final Certificate selfSignedCert = generate(InetAddress.getLocalHost().getCanonicalHostName(), keyPair);

    ks.setCertificateEntry("alias.cert", selfSignedCert);
    ks.setKeyEntry("alias.key", keyPair.getPrivate(), new char[0], new Certificate[] { selfSignedCert });
    return ks;
}

private static Certificate generate(final String fqdn, final KeyPair keypair) throws Exception {
    final Instant now = Instant.now();
    final Date notBefore = Date.from(now.minus(24, ChronoUnit.HOURS));
    final Date notAfter = Date.from(now.plus(24, ChronoUnit.HOURS));
    final ContentSigner selfSigner = new JcaContentSignerBuilder("SHA256WithRSAEncryption")
        .build(keypair.getPrivate());
    final X500Name owner = new X500Name("CN=" + fqdn);
    final X509CertificateHolder certHolder = new JcaX509v3CertificateBuilder(
        /*issuer*/owner,
        /*serial*/new BigInteger(64, new SecureRandom()),
        /*cert start*/notBefore,
        /*cert end*/notAfter,
        /*subject*/owner,
        /*subject public key*/keypair.getPublic())
        .build(selfSigner);
    final X509Certificate selfSignedCert = new JcaX509CertificateConverter()
        .getCertificate(certHolder);
    selfSignedCert.verify(keypair.getPublic()); // Certificate is good!
    return selfSignedCert;
}
