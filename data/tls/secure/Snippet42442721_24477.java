        // Set up Client Cert settings
        KeyStore clientCertStore = KeyStore.getInstance("JKS");
        clientCertStore.load(new FileInputStream(clientKeystoreLocation), clientKeystorePassword);            

        // Create temporary one keystore, then extract the client cert using it's alias from keystore.jks, then create
        // a new keystore with this cert, that the process will use to connect with.
        KeyStore tempKstore = KeyStore.getInstance("JKS");
        tempKstore.load(null);
        tempKstore.setKeyEntry(certificateAlias, clientCertStore.getKey(certificateAlias, bwConfig.clientKeystorePassword),
                clientKeystorePassword, clientCertStore.getCertificateChain(certificateAlias));
        clientCertStore = tempKstore;

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientCertStore, clientKeystorePassword);            

        // Set up Truststore settings
        File truststoreFile = new File(TrustStoreLocation);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(truststoreFile), TrustStorePassword);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        // Set to TLS 1.2 encryption
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        SSLSocketFactory ssf = sslContext.getSocketFactory();
        ssf.createSocket(serviceURL.getHost(), servicePort);

        bp.getRequestContext().put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", ssf);
