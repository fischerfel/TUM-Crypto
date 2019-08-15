     CertificateFactory cf = CertificateFactory.getInstance("X.509");

        InputStream caInputMmx = new BufferedInputStream(this.getAssets().open("123.crt"));
        Certificate caMmx = cf.generateCertificate(caInputMmx);
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("caMmx", caMmx);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        client.setSslSocketFactory(context.getSocketFactory());
