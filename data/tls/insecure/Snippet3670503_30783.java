        KeyStore identityStore = KeyStore.getInstance(KeyStore.getDefaultType());
        ClassPathResource keystore = new ClassPathResource(cadaBackendCertFile);

        identityStore.load(keystore.getInputStream(), cadaBackendCertFilePassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(identityStore, cadaBackendCertFilePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(identityStore);

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        SSLSocketFactory fac = ctx.getSocketFactory();
        Socket sslsock = fac.createSocket(cadaBackendEndpoint, cadaBackendPort);
        TTransport transport = new TSocket(sslsock);
