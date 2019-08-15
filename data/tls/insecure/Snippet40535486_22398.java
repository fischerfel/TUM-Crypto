        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");

    // From https://www.washington.edu/itconnect/security/ca/load-der.crt
    InputStream caInput = context.getResources().openRawResource(R.raw.ssl_bundle);
    Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    SSLContext sslContext = SSLContext.getInstance("TLSv1");
    sslContext.init(null, tmf.getTrustManagers(), null);

    return  new NoSSLv3SocketFactory(sslContext.getSocketFactory());
