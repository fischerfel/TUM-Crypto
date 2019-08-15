        // Load Certificate
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate certificate = certificateFactory.generateCertificate(new FileInputStream(new File("C:/Users/prabhuj/workspace5/sample/bin/certificate.cer")));

        // Create TrustStore
        KeyStore trustStoreContainingTheCertificate = KeyStore.getInstance("JKS");
        trustStoreContainingTheCertificate.load(null, null);

        trustStoreContainingTheCertificate.setCertificateEntry("cert", certificate);

        // Create SSLContext
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStoreContainingTheCertificate);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

       HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;           
    SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
